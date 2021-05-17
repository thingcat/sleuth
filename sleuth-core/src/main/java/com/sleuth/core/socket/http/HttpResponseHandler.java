package com.sleuth.core.socket.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.HttpHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class HttpResponseHandler implements HttpHandler<FullHttpResponse> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handler(ChannelHandlerContext context, FullHttpResponse response) {
		this.handler(context, response, null);
	}

	@Override
	public void handler(ChannelHandlerContext context, FullHttpResponse response,
			WebSocketServerHandshakerFactory handshakerFactory) {
		
		final FullHttpResponse httpResponse = (FullHttpResponse) response;
		String err = "Unexpected FullHttpResponse (getStatus=" + httpResponse.status() + ", content="
				+ httpResponse.content().toString(CharsetUtil.UTF_8) + ')';
		logger.warn(err);
		throw new RuntimeException("Invalid");
	}

}
