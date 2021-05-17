package com.sleuth.core.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/** 通道服务
 * 
 * @author Administrator
 *
 */
public interface HttpHandler<T> {
	
	/** 服务处理
	 * 
	 * @param ctx
	 * @param object
	 * @param handshakerFactory
	 */
	public abstract void handler(ChannelHandlerContext context, T object, 
			WebSocketServerHandshakerFactory handshakerFactory);
	
	/** 服务处理
	 * 
	 * @param ctx
	 * @param object
	 */
	public abstract void handler(ChannelHandlerContext context, T object);
	
}
