package com.sleuth.core.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/** 心跳包
 * 
 * @author Jonse
 * @date 2019年12月23日
 */
public abstract class IdleStateTriggerAdapter extends ChannelInboundHandlerAdapter {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private WebSocketClientAdapter adapterService;
	
	public IdleStateTriggerAdapter(WebSocketClientAdapter adapterService) {
		this.adapterService = adapterService;
	}
	
	protected void handlerWriterIdle(ChannelHandlerContext ctx) {
    	this.adapterService.onPong(ctx.channel());
    	logger.debug(" ---WRITER_IDLE---{}", ctx.channel().remoteAddress().toString());    
    }
	
}
