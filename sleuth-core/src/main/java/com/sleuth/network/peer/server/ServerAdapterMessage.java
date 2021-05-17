package com.sleuth.network.peer.server;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.ErrProtocol;
import com.sleuth.network.message.protocol.EvType;
import com.sleuth.network.message.protocol.OkProtocol;
import com.sleuth.network.message.protocol.SubProtocol;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.protocol.UnsubProtocol;
import com.sleuth.network.peer.AdapterMessage;

import io.netty.channel.Channel;

/** 服务端消息处理
 * 
 * @author Jonse
 * @date 2021年1月17日
 */
@Component("serverAdapterMessage")
public class ServerAdapterMessage implements AdapterMessage {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private MessageManager messageManager;
	
	//订阅请求处理
	@Resource(name="serverSubscribeHandle")
	private MessageHandle<SubProtocol> serverSubscribeHandle;
	//取消订阅请求处理
	@Resource(name="serverUnsubscribeHandle")
	private  MessageHandle<UnsubProtocol> serverUnsubscribeHandle;
	//申请同步数据请求处理
	@Resource(name="serverApplyHandle")
	private MessageHandle<ApProtocol> serverApplyHandle;
	//数据类消息处理
	@Resource(name="tableMessageHandle")
	private MessageHandle<TabProtocol> tableMessageHandle;
	//完成任务消息
	@Resource(name="finishedHandle")
	private MessageHandle<OkProtocol> finishedHandle;
	//错误消息
	@Resource(name="errorHandler")
	private MessageHandle<ErrProtocol> errorHandler;
	
	@Override
	public void onAdapter(Channel channel, String msgText) {
		
		logger.debug("msg = {}", msgText);
		
		try {
			//将消息转换为JSON
			JSONObject object = JSON.parseObject(msgText);
			String event = object.getString("event");
			
			//如果是请求订阅消息
			if (EvType.subscribe.getEvent().equals(event)) {
				SubProtocol message = JSON.parseObject(msgText, SubProtocol.class);
				this.serverSubscribeHandle.handle(messageManager, channel, message);
				
			}
			//请求取消订阅
			else if (EvType.unsubscribe.getEvent().equals(event)) {
				UnsubProtocol message = JSON.parseObject(msgText, UnsubProtocol.class);
				this.serverUnsubscribeHandle.handle(messageManager, channel, message);
			}
			//申请同步数据请求处理
			else if (EvType.apply.getEvent().equals(event)) {
				ApProtocol message = JSON.parseObject(msgText, ApProtocol.class);
				this.serverApplyHandle.handle(messageManager, channel, message);
			}
			//数据类消息处理
			else if (EvType.table.getEvent().equals(event)) {
				TabProtocol message = JSON.parseObject(msgText, TabProtocol.class);
				this.tableMessageHandle.handle(messageManager, channel, message);
			}
			//完成消息的返回
			else if (EvType.ok.getEvent().equals(event)) {
				OkProtocol message = JSON.parseObject(msgText, OkProtocol.class);
				this.finishedHandle.handle(this.messageManager, channel, message);
			}
			//错误消息返回
			else if (EvType.error.getEvent().equals(event)) {
				ErrProtocol message = JSON.parseObject(msgText, ErrProtocol.class);
				this.errorHandler.handle(this.messageManager, channel, message);
			}
			//未知消息不用处理
			else {
				logger.warn("Unknown message, msg = {}", msgText);
			}
		} catch (Exception e) {
			logger.error("Error message, msg = {}", msgText, e);
			
		}
		
	}

}
