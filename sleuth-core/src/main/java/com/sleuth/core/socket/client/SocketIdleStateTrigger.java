package com.sleuth.core.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/** 心跳包
 * 
 * @author Jonse
 * @date 2019年12月23日
 */
public class SocketIdleStateTrigger extends IdleStateTriggerAdapter {

	public SocketIdleStateTrigger(WebSocketClientAdapter adapterService) {
		super(adapterService);
	}
	
	@Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
    	if (evt instanceof IdleStateEvent) {  
            IdleState state = ((IdleStateEvent) evt).state();  
            if (state == IdleState.WRITER_IDLE) {
                this.handlerWriterIdle(ctx);
            }  
        } else {  
            super.userEventTriggered(ctx, evt);  
        }  
    } 

}
