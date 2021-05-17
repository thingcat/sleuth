package com.sleuth.network.message.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.protocol.ErrProtocol;

import io.netty.channel.Channel;

/** 客户端处理来自服务端返回的错误消息
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("errorHandler")
public class ErrorHandler implements MessageHandle<ErrProtocol> {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(MessageManager manager, Channel channel, ErrProtocol protocol) {
		logger.warn("Error msg={}", JSON.toJSONString(protocol));
	}

}
