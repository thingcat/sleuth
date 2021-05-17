package com.sleuth.core.socket.message;

import io.netty.channel.Channel;

/** 通道消息管理
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
public interface MessageManager {
	
	/** 服务端向指定频道广播消息
	 * 
	 * @param ch
	 * @param message
	 */
	public abstract <T extends Protocol> void push(T message);
	
	/** 客户端向服务端发送消息
	 * @param <T>
	 * 
	 * @param message
	 */
	public abstract <T extends Protocol> void send(T message);
	
	/** 向指定通道发送消息
	 * 
	 * @param channel
	 * @param message
	 */
	public abstract <T extends Protocol> void send(Channel channel, T message);
	
}
