package com.sleuth.core.socket.message;

import java.net.URI;

import com.sleuth.core.socket.SocketListener;

import io.netty.channel.Channel;

/** 客户端通道消息处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public interface ClientSocketListener extends SocketListener {
	
	/** 失败的连接
	 * 
	 */
	public abstract void onFailure(URI uri);
	
	/** 接收pong消息
	 * 
	 * @param channel
	 */
	public abstract void onPong(Channel channel);
	
}
