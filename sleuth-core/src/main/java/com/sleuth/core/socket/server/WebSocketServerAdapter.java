package com.sleuth.core.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.message.ServerSocketListener;
import com.sleuth.core.utils.MsgUtil;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/** 服务端消息服务
 * 
 * @author Jonse
 * @date 2019年12月23日
 */
public abstract class WebSocketServerAdapter implements ServerSocketListener {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String pong = "pong";
	static final String ping = "ping";
	
	/** 消息服务
	 * 心跳包只接收ping消息，不接受pong
	 */
	@Override
	public void serivce(Channel channel, Object receive) {
		if (receive instanceof PingWebSocketFrame) {
			this.onPing(channel);
		} else if (receive instanceof CloseWebSocketFrame) {
        	this.onClose(channel);
        } else if(receive instanceof TextWebSocketFrame){
        	String text = ((TextWebSocketFrame) receive).text();
        	if (ping.equals(text)) {
				this.onPing(channel);
			} else if ("close".equals(text)) {
				this.onClose(channel);
			} else {
				this.onMessage(channel, text);
			}
		} else if (receive instanceof BinaryWebSocketFrame) {
			String text = MsgUtil.decode(receive);
			if (ping.equals(text)) {
				this.onPing(channel);
			} else if ("close".equals(text)) {
				this.onClose(channel);
			} else {
				this.onMessage(channel, text);
			}
		}
	}
	
	@Override
	public void onAdded(Channel channel) {
		logger.debug("try to establish a connection -> {}", channel.remoteAddress());
		CLI.add(channel);
	}

	@Override
	public void onClose(Channel channel) {
		logger.debug("already disconnected -> {}", channel.remoteAddress());
		channel.close();
		CLI.remove(channel);
	}

	@Override
	public void onCaught(Channel channel, Throwable cause) {
		logger.error("Throwable {}", cause.getMessage());
		channel.close();
		CLI.remove(channel);
	}

	@Override
	public void onPing(Channel channel) {
		channel.writeAndFlush(new TextWebSocketFrame(pong));
		logger.debug("---RECEIVE_IDLE---",  channel.remoteAddress().toString());
	}

}
