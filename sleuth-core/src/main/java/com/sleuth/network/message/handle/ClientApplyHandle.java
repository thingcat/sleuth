package com.sleuth.network.message.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.protocol.ApProtocol;

import io.netty.channel.Channel;

/** 处理来自客户端的申请数据请求消息
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("clientApplyHandle")
public class ClientApplyHandle implements MessageHandle<ApProtocol> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(MessageManager manager, Channel channel, ApProtocol message) {
		// 受理消息，服务端接受了数据同步的请求，并返回的消息
		if (message.getStage() == 1) {
			logger.info("Successfully accepted synchronization request.");
		} else {
			logger.info("Failed to accept synchronization request.");
		}
	}

}
