package com.sleuth.network.message.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.message.protocol.ErrProtocol;
import com.sleuth.network.message.protocol.SubProtocol;

import io.netty.channel.Channel;

/** 处理来自客户端的取消订阅消息
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("serverUnsubscribeHandle")
public class ServerUnsubscribeHandle implements MessageHandle<SubProtocol> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(MessageManager manager, Channel channel, SubProtocol message) {
		if (message.getStage() == 0) {
			//将阶段增加一个，表示接收并处理，将处理结果返回
			message.setStage(message.getStage() + 1);
			manager.send(channel, message);
			CLI.remove(message.getCh(), channel);
			logger.error("remove subscribe ch = {}, cli = {}", message.getCh(), channel.remoteAddress().toString());
		} else {
			ErrProtocol err = ErrProtocol.newProtocol("102", "Invalid stage", message.getCh());
			manager.send(channel, err);
		}
	}

}
