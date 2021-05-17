package com.sleuth.network.message.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.protocol.SubProtocol;

import io.netty.channel.Channel;

/** 客户端处理来自服务端的订阅消息
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("clientUnsubscribeHandle")
public class ClientUnsubscribeHandle implements MessageHandle<SubProtocol> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(MessageManager manager, Channel channel, SubProtocol message) {
		//表示订阅成功
		if (message.getStage() == 1) {
			logger.info("Unsubscription successful, ch = {}", message.getCh());
		} else {
			logger.info("Unsbscription unsuccessful, ch = {}", message.getCh());
		}
	}

}
