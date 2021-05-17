package com.sleuth.network.peer.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.peer.PeerCallback;

import io.netty.channel.Channel;

@Component("serverCallback")
public class ServerCallback implements PeerCallback {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(Channel channel) {
		logger.info("publish node subscription service............................");
		//发布订阅服务
		CmdType[] chs = CmdType.values();
		for (CmdType ch : chs) {
			if (ch.getOp().equals("push")) {
				logger.info("publish subscribe service, ch = {}", ch.getCh());
				//注册通道
				CLI.register(ch.getCh());
			}
		}
	}
	
}
