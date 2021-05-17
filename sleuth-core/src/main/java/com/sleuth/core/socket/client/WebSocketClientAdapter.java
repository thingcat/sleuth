package com.sleuth.core.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.message.ClientSocketListener;
import com.sleuth.core.utils.MsgUtil;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/** 客户端消息通道处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public abstract class WebSocketClientAdapter implements ClientSocketListener {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String ping = "ping";
	static final String pong = "pong";
	
	/** 消息服务
	 * 
	 * @param channel
	 * @param result
	 */
	@Override
	public void serivce(Channel channel, Object result) {
		if(result instanceof PongWebSocketFrame) {
        	this.onPong(channel);
        } else if (result instanceof CloseWebSocketFrame) {
        	this.onClose(channel);
        } else if(result instanceof TextWebSocketFrame){
        	String text = ((TextWebSocketFrame) result).text();
        	if (!pong.equalsIgnoreCase(text)) {
				this.onMessage(channel, text);
			}
		} else if (result instanceof BinaryWebSocketFrame) {
			String text = MsgUtil.decode(result);
			if (!pong.equalsIgnoreCase(text)) {
				this.onMessage(channel, text);
			}
		}
	}
	
	@Override
	public void onClose(Channel channel) {
		logger.debug("{} -> already disconnected", channel.remoteAddress());
		channel.close();
	}

	@Override
	public void onCaught(Channel channel, Throwable cause) {
		logger.error("Throwable {}", cause.getMessage());
		channel.close();
	}

	@Override
	public void onPong(Channel channel) {
		channel.writeAndFlush(new TextWebSocketFrame(ping));
//		logger.debug("PONG_IDLE --> to {}", channel.remoteAddress().toString());
	}
	
	/** 执行连接
	 * 
	 */
	public abstract void doConnect();
	
}
