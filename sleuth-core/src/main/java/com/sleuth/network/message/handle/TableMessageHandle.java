package com.sleuth.network.message.handle;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.protocol.TabProtocol;

import io.netty.channel.Channel;

/** 接收数据消息
 * 
 * 服务端和客户端共用
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("tableMessageHandle")
public class TableMessageHandle implements MessageHandle<TabProtocol> {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TableProtocolConsumer tableConsumer;
	
	@Override
	public void handle(MessageManager manager, Channel channel, TabProtocol message) {
		logger.debug("Receive table message: {}", message.toString());
		this.tableConsumer.push(message);
	}

}
