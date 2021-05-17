package com.sleuth.core.socket.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.server.CLI;

import io.netty.channel.Channel;

/** 消息管理
 * 
 * @author Jonse
 * @date 2020年10月15日
 */
@Component
public class SocketMessageManager implements MessageManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 服务端向指定频道广播消息
	 * @param <T>
	 * 
	 * @param message 消息
	 */
	public <T extends Protocol> void push(T message) {
		CLI.push(message);
	}
	
	/** 客户端向服务端发送消息
	 * 
	 * @param message 消息
	 */
	@Override
	public <T extends Protocol> void send(T message) {
		Channel channel = CLI.get();
		if (channel == null) {
			logger.warn("There is no connection channel established with the server, unable to send messages.");
		} else {
			CLI.push(channel, message);
		}
	}

	@Override
	public <T extends Protocol> void send(Channel channel, T message) {
		CLI.push(channel, message);
	}
	
}
