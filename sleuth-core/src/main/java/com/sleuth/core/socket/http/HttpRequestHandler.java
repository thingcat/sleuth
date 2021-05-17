package com.sleuth.core.socket.http;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.HttpHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class HttpRequestHandler implements HttpHandler<FullHttpRequest> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private WebSocketServerHandshaker handshaker;

	@Override
	public void handler(ChannelHandlerContext context, FullHttpRequest request,
			WebSocketServerHandshakerFactory handshakerFactory) {
		
		// 如果HTTP解码失败，返回HHTP异常
		if (!request.decoderResult().isSuccess() 
				|| (!"websocket".equals(request.headers().get("Upgrade")))) {
			this.sendHttpResponse(context, request, 
					new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			logger.warn("HTTP decoding failed !");
			return;
		}
		
		if (handshakerFactory != null) {
			// 构造握手响应返回
			handshaker = handshakerFactory.newHandshaker(request);
			if (handshaker == null) {
				// 响应不支持的请求
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
				logger.warn("Respond to an unsupported request!");
			} else {
				handshaker.handshake(context.channel(), request);
			}
		}
	}
	
	@Override
	public void handler(ChannelHandlerContext context, FullHttpRequest request) {
		this.handler(context, request, null);
	}
	
	/** 发送异常
	 * 
	 * @param ctx
	 * @param request
	 * @param response
	 */
	private void sendHttpResponse(ChannelHandlerContext context, 
			FullHttpRequest request, FullHttpResponse response) {
		
		// 返回应答给客户端
		if (response.status().code() != 200) {
			ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(byteBuf);
			byteBuf.release();
			HttpUtil.setContentLength(response, response.content().readableBytes());
		}
		
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = context.channel().writeAndFlush(response);
		if (!HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

}
