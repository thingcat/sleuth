package com.sleuth.core.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;

/** 客户端通道处理
 * 
 * @author Jonse
 * @date 2019年12月16日
 */
@Sharable
public class ClientChannelInboundHandler extends SimpleChannelInboundHandler<Object> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final WebSocketClientHandshaker handshaker;
	private final WebSocketClientAdapter adapterService;
	private ChannelPromise handshakeFuture;
	
	public ClientChannelInboundHandler(WebSocketClientHandshaker handshaker, WebSocketClientAdapter adapterService) {
		this.handshaker = handshaker;
		this.adapterService = adapterService;
	}
	
	public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }
	
	/**
     * 当客户端主动链接服务端的链接后，调用此方法
     *
     * @param channelHandlerContext ChannelHandlerContext
     * 
     */
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		handshaker.handshake(ctx.channel());
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object result) throws Exception {
		
		final Channel channel = ctx.channel();
		
		if (!handshaker.isHandshakeComplete()) {
			handshaker.finishHandshake(channel, (FullHttpResponse) result);
            handshakeFuture.setSuccess();
            return;
        }
		
		if (result instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) result;
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
		//消息处理
		this.adapterService.serivce(channel, result);
	}
	
	/** 异常处理，有异常直接断开
	 * 
	 */
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
		adapterService.onCaught(ctx.channel(), cause);
		logger.error("An exception occurred in the connection channel, cause: {}", cause.getMessage());
		this.handlerRemoved(ctx);
    }
	
	/** 断开连接处理
	 * 
	 */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
    	logger.warn("Disconnect from server = {}", ctx.channel().remoteAddress());
    	ctx.fireChannelInactive();
    	ctx.close();
    	adapterService.onClose(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }
	
}
