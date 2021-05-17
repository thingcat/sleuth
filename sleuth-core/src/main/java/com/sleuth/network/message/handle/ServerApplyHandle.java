package com.sleuth.network.message.handle;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.CmdAcceptor;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.ErrProtocol;

import io.netty.channel.Channel;

/** 处理来自客户端的申请数据请求消息
 * 
 * @author Jonse
 * @date 2020年10月17日
 */
@Component("serverApplyHandle")
public class ServerApplyHandle implements MessageHandle<ApProtocol> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private CmdAcceptor cmdAcceptor;
	
	@Override
	public void handle(MessageManager manager, Channel channel, ApProtocol message) {
		if (message.getStage() == 0) {
			this.cmdAcceptor.onAccept(channel, message);
		} else {
			ErrProtocol err = ErrProtocol.newProtocol("102", "Invalid stage", message.getCh());
			manager.send(channel, err);
		}
	}

}
