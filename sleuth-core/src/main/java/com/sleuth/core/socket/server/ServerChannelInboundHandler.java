package com.sleuth.core.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.HttpHandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/** 接收来自客户端的消息处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
@Sharable
public class ServerChannelInboundHandler extends SimpleChannelInboundHandler<Object> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final WebSocketServerAdapter socketAdapter;
	private final WebSocketServerHandshakerFactory handshakerFactory;
	
	private final HttpHandler<FullHttpRequest> httpRequestHandler;
	private final HttpHandler<FullHttpResponse> httpResponseHandler;
	
	public ServerChannelInboundHandler(WebSocketServerAdapter socketAdapter, WebSocketServerHandshakerFactory handshakerFactory, 
			HttpHandler<FullHttpRequest> httpRequestHandler, HttpHandler<FullHttpResponse> httpResponseHandler) {
		this.socketAdapter = socketAdapter;
		this.handshakerFactory = handshakerFactory;
		this.httpRequestHandler = httpRequestHandler;
		this.httpResponseHandler = httpResponseHandler;
	}
	
	/**
	 * 接收来自客户端的消息
	 * 
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object receive) throws Exception {
		try {
			// 传统的HTTP接入
			if (receive instanceof FullHttpRequest) {
				FullHttpRequest request = (FullHttpRequest) receive;
				httpRequestHandler.handler(ctx, request, handshakerFactory);
				return;
			}
			
			if (receive instanceof FullHttpResponse) {
				FullHttpResponse response = (FullHttpResponse) receive;
				httpResponseHandler.handler(ctx, response);
				return;
			}
			//WebSocket 消息处理
			this.socketAdapter.serivce(ctx.channel(), receive);
			
		} catch (Exception e) {
			logger.error("Unknown error occurred in message processing!!!", e);
			this.socketAdapter.onCaught(ctx.channel(), e);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		this.socketAdapter.onAdded(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		this.socketAdapter.onClose(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		this.socketAdapter.onCaught(ctx.channel(), cause);
	}

}
