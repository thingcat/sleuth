package com.sleuth.network.peer.callback;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.BlockChain;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.dto.NodeInfoDTO;
import com.sleuth.network.message.CmdApply;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.Op;
import com.sleuth.network.message.protocol.SubProtocol;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.sync.apply.BlockApplyTimer;
import com.sleuth.network.peer.PeerCallback;
import com.sleuth.network.peer.PeerNetwork;
import com.sleuth.network.schema.NodeInfo;

import io.netty.channel.Channel;

@Component("clientCallback")
public class ClientCallback implements PeerCallback {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private CmdApply cmdApply;
	@Resource
	private BlockChain blockChain;
	@Resource
	private PeerNetwork peerNetwork;
	@Resource
	private MessageManager messageManager;
	
	@Override
	public void handle(Channel channel) {
		//上报节点
		this.doReport();
		//请求订阅服务
		this.doSubscribe();
		//申请区块同步
		new Timer().schedule(new BlockApplyTimer(cmdApply, blockChain), 2000);
	}
	
	/** 向主机发送节点信息
	 * 
	 */
	private void doReport() {
		Channel channel = CLI.get();
		if (channel != null) {
			NodeInfo nodeInfo = this.peerNetwork.getLocalNodeInfo();
			NodeInfoDTO data = CopyUtil.copyProperty(nodeInfo, NodeInfoDTO.class);
			TabProtocol message = TabProtocol.newProtocol(CmdType.NODE_PUSH);
			message.setData(JSON.toJSONString(data));
			CLI.push(channel, message);
			logger.debug("submit localInfo, {}", nodeInfo.toString());
		}
	}
	
	/** 向目标主机发起消息推送申请订阅服务
	 * 
	 */
	private void doSubscribe() {
		CmdType[] chs = CmdType.values();
		for (CmdType ch : chs) {
			//申请消息推送的订阅服务
			if (ch.getOp().equals(Op.push.getOp())) {
				SubProtocol protocol = SubProtocol.newProtocol(ch);
				logger.info("Apply subscribe: ch = {}", ch.getCh());
				this.messageManager.send(protocol);
				try {
					TimeUnit.MILLISECONDS.sleep(100);//暂停100毫秒
				} catch (Exception e) {
				}
			}
		}
	}
	
}
