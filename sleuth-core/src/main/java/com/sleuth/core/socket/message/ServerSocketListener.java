package com.sleuth.core.socket.message;

import com.sleuth.core.socket.SocketListener;

import io.netty.channel.Channel;

/** 服务端通道消息处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public interface ServerSocketListener extends SocketListener {
	
	/** 加入通道
	 * 
	 * @param channel
	 * 
	 */
	public abstract void onAdded(Channel channel);
	
	/** 接受ping消息
	 * 
	 * @param channel
	 */
	public abstract void onPing(Channel channel);
	
}
