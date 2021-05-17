package com.sleuth.core.socket.message;

import io.netty.channel.Channel;

/** 消息处理
 * 
 * @author Administrator
 * @param <T>
 *
 */
public interface MessageHandle<T extends Protocol> {
	
	/** 接收消息处理
	 * @param <T>
	 * 
	 * @param <T>
	 * 
	 * @param channel 连接通道
	 * @param event 事件
	 * @param ch 消息频道
	 * @param data 消息数据
	 */
	public abstract void handle(MessageManager manager, Channel channel, T message);
	
}
