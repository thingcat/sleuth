package com.sleuth.network.peer.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.core.socket.client.WebSocketClient;
import com.sleuth.core.socket.client.WebSocketClientAdapter;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.peer.AdapterMessage;
import com.sleuth.network.peer.PeerCallback;
import com.sleuth.network.peer.PeerNetwork;
import com.sleuth.network.peer.hash.NodeId;
import com.sleuth.network.schema.NodeInfo;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/** 客户端消息通道处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public class ClientAdapterService extends WebSocketClientAdapter {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final PeerNetwork peerNetwork;
	private final Timer timer;
	private final NodeInfo localNode;//本机节点
	
	private NodeInfo targetNode;//目标节点
	
	private List<NodeInfo> nearNodes;
	//连接过的节点
	private final List<NodeInfo> nodesConnected = new ArrayList<NodeInfo>();
	private volatile int i = 0;
	
	private final AdapterMessage adapterMessage;
	private final PeerCallback peerCallback;
	
	private final Timer timerCallback = new HashedWheelTimer();
	
	public ClientAdapterService(PeerNetwork peerNetwork, PeerCallback peerCallback, AdapterMessage adapterMessage, Timer timer) {
		this.peerNetwork = peerNetwork;
		this.adapterMessage = adapterMessage;
		this.peerCallback = peerCallback;
		this.timer = timer;
		
		//本机节点
		this.localNode = this.peerNetwork.getLocalNodeInfo();
		this.nearNodes = this.peerNetwork.findNearNodes(new NodeId(this.localNode.getNid()));
	}
	
	@Override
	public void onMessage(Channel channel, String msgText) {
		JSONObject jsonObject = JSON.parseObject(msgText);
		jsonObject.put("id", channel.id().asShortText());
		this.adapterMessage.onAdapter(channel, msgText);
	}

	@Override
	public void onSuccess(Channel channel) {
		logger.info("The server has been successfully connected!!!");
		this.timerCallback.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				peerCallback.handle(channel);
			}
		}, 5, TimeUnit.SECONDS);
		//连接成功后，清除掉已连接过的
		this.nodesConnected.clear();
	}

	@Override
	public void onFailure(URI uri) {
		logger.warn("{} host connection failed, 2 seconds later the next host connection.", uri);
		//放入已加入的节点中
		this.nodesConnected.add(this.targetNode);
		this.i++;
		this.doConnect();
	}
	
	@Override
	public void doConnect() {
		//如果客户端已经连接上了，那么就不连接了
		if (CLI.get() != null) {
			logger.warn("It's connected to {}", CLI.get().toString());
			return;
		}
		
		//从附近的节点里面寻找连接
		if (this.nearNodes == null || this.nearNodes.size() == 0) {
			logger.warn("No nearby nodes found, please import seed.json configuration file.");
			return;
		}
		
		try {
			this.targetNode = this.nearNodes.get(this.i);
		} catch (IndexOutOfBoundsException e) {
			logger.warn("The nearby nodes have been used up.");
			return;
		}
		//判断是否已经连接过了
		if (this.nodesConnected.contains(this.targetNode)) {
			this.i++;
			//如果超过32次，则客户端的建立
			if (this.nodesConnected.size() >= 32) {
				logger.warn("Client connection failure!");
			} else {
				this.doConnect();
			}
		} else {
			logger.debug("Try to connect to the target host, URI={}, WEIGHT={}", this.targetNode.getUri(), this.targetNode.getWeight());
			//判断两个主机之间是否已经建立了连接通道
			Channel channel = CLI.findChannel(this.targetNode);
			//如果没有建立连接通道，并且不是同一台主机，则可以进行连接
			URI localUri = localNode.toUri();
			URI nodeUri = this.targetNode.toUri();
			if (channel == null && !localUri.getHost().equals(nodeUri.getHost()) && this.targetNode.getWeight() > -1) {
				//2秒后，开始连接
				logger.debug("Start connecting to the target host in 2 seconds.");
				this.timer.newTimeout(new ClientPeerRunner(new WebSocketClient(nodeUri, this)), 2, TimeUnit.SECONDS);
				return;
			}
			
			logger.debug("Invalid target host.");
			//放入已加入的节点中
			this.nodesConnected.add(this.targetNode);
			this.i++;
			//本组附近的节点已经消耗完，则开始下一轮
			if (this.i == this.nearNodes.size()) {
				NodeInfo lastNodeInfo = this.nearNodes.get(this.nearNodes.size() - 1);
				List<NodeInfo> result = this.peerNetwork.findNearNodes(new NodeId(lastNodeInfo.getNid()));
				this.nearNodes.clear();
				this.nearNodes.addAll(result);
				this.i = 0;
			}
			this.doConnect();
		}
	}
	
}
