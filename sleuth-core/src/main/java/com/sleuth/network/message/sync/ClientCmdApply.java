package com.sleuth.network.message.sync;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.core.socket.message.Protocol;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.message.CmdApply;
import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;

import io.netty.channel.Channel;

/** 发起人的同步器
 * 
 * 1、申请同步
 * 2、接受服务端的响应
 * 3、处理服务端同意/拒绝响应的请求
 * 4、处理数据
 * 
 * @author Jonse
 * @date 2021年1月17日
 */
@Component
public class ClientCmdApply implements CmdApply {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private TableProtocolConsumer tableConsumer;
	
	@Override
	public void onApply(CmdType cmd) {
		Channel channel = CLI.get();
		if (channel != null) {
			ApProtocol message = ApProtocol.newProtocol(cmd);
			CLI.push(channel, message);
			logger.info("Apply for {} synchronization, waiting for response, protocol={}", cmd.getCh(), message.toString());
		} else {
			logger.warn("No target node was found!");
		}
	}
	
	@Override
	public void onApply(CmdType cmd, Object data) {
		Channel channel = CLI.get();
		if (channel != null) {
			ApProtocol message = ApProtocol.newProtocol(cmd);
			message.setArgs(JSON.toJSONString(data));
			CLI.push(channel, message);
			logger.info("Apply for {} synchronization, waiting for response, protocol={}", cmd.getCh(), message.toString());
		} else {
			logger.warn("No target node was found!");
		}
	}

	@Override
	public <T extends Protocol> void onTable(MessageManager manager, TabProtocol message) {
		this.tableConsumer.push(message);
	}

}
