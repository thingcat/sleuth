package com.sleuth.core.socket;

import io.netty.channel.Channel;

/** Listener
 * 
 * @author Administrator
 *
 */
public interface SocketListener {

	/** 消息服务
	 * 
	 * @param channel
	 * @param text
	 */
	public abstract void serivce(Channel channel, Object text);
	
	/** 消息管理
	 * 
	 * @param channel
	 * @param text
	 */
	public abstract void onMessage(Channel channel, String msgText);
	
	/** 通道关闭管理
	 * 
	 * @param channel
	 */
	public abstract void onClose(Channel channel);
	
	/** 异常管理
	 * 
	 * @param channel
	 * @param cause
	 */
	public abstract void onCaught(Channel channel, Throwable cause);
	
	/** 成功后的回调
	 * 
	 * @param channel
	 */
	public abstract void onSuccess(Channel channel);
	
}
