package com.sleuth.network.peer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.api.dto.NodeDTO;
import com.sleuth.core.http.HttpRequest;
import com.sleuth.core.http.HttpResult;
import com.sleuth.core.http.request.DefaultHttpRequest;
import com.sleuth.core.socket.WebSocket;
import com.sleuth.core.socket.client.WebSocketClientAdapter;
import com.sleuth.core.socket.server.WebSocketServer;
import com.sleuth.core.socket.server.WebSocketServerAdapter;
import com.sleuth.network.PeerRunner;
import com.sleuth.network.PeerService;
import com.sleuth.network.peer.client.ClientAdapterService;
import com.sleuth.network.peer.server.ServerAdapterService;
import com.sleuth.network.peer.server.ServerPeerRunner;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.seed.SeedLoader;
import com.sleuth.network.service.NodeInfoService;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

@Service
public class PeerAppService implements PeerService {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Timer timerServer = new HashedWheelTimer();
	final Timer timerClient = new HashedWheelTimer();
	
	@Resource
	private SeedLoader seedLoader;
	@Resource
	private PeerNetwork peerNetwork;
	
	@Resource(name="clientCallback")
	private PeerCallback clientCallback;
	@Resource(name="serverCallback")
	private PeerCallback serverCallback;
	
	@Resource(name="clientAdapterMessage")
	private AdapterMessage clientAdapterMessage;
	@Resource(name="serverAdapterMessage")
	private AdapterMessage serverAdapterMessage;
	
	private WebSocket webSocketServer;
	private WebSocket webSocketClient;
	
	@Value("${network.api.uri:/}")
	private String path;
	
	final HttpRequest httpRequest = new DefaultHttpRequest();
	
	@Resource
	private NodeInfoService nodeInfoService;
	
	@Override
	public List<NodeInfo> download() {
		try {
			URI uri = new URI(this.path + "/seed/list");
			logger.info("API address, {}", this.path);
			HttpResult httpResult = httpRequest.get(uri.toString());
			if (httpResult.getStatusCode() == 200) {
				JSONObject json = httpResult.getJSONObject();
				JSONArray data = json.getJSONArray("data");
				if (data != null && data.size() > 0) {
					int len = data.size();
					List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
					for(int i=0; i<len; i++) {
						NodeDTO node = data.getObject(i, NodeDTO.class);
						NodeInfo nodeInfo = new NodeInfo(node);
						//保存到本地数据库中
						this.nodeInfoService.add(nodeInfo);
						nodeInfos.add(nodeInfo);
					}
					return nodeInfos;
				}
			}
		} catch (URISyntaxException e) {
			logger.warn("Invalid API address.", e);
		}
		return null;
	}
	
	@Override
	public void start() {
		
		//导入种子
		this.seedLoader.unpack();
		
		//初始化网络模型，将所有的节点导入到网络模型中
		this.peerNetwork.init();
		
		//获取本机节点信息
		NodeInfo localNode = this.peerNetwork.getLocalNodeInfo();
		
		// 启动服务端
        logger.info("Start server network service......");
        WebSocketServerAdapter serverAdapterService = new ServerAdapterService(this.serverCallback, this.serverAdapterMessage);
        
        this.webSocketServer = new WebSocketServer(localNode.toUri(), serverAdapterService);
        PeerRunner serverRunner = new ServerPeerRunner(this.webSocketServer);
        this.timerServer.newTimeout(serverRunner, 1, TimeUnit.SECONDS);
		
		//创建客户端连接对象
        logger.info("Start client network service......");
		WebSocketClientAdapter clientAdapter = new ClientAdapterService(this.peerNetwork, this.clientCallback, this.clientAdapterMessage, this.timerClient);
    	clientAdapter.doConnect();
	}
	
	@Override
	public void destroy() {
		//销毁客户端
		this.webSocketClient.destroy();
		this.timerClient.stop();
		logger.info("Destroy WebSocket client!");
		//销毁服务端
		this.webSocketServer.destroy();
		logger.info("Destroy WebSocket server!");
	}
	
}
