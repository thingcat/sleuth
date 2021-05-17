package com.sleuth.network.message.queue;

import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.protocol.TabProtocol;

/** 数据处理器
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
public interface TableHandler {

	/** 处理协议消息
	 * 
	 * @param tableHandler
	 * @param message
	 */
	public abstract void handler(TableProtocolConsumer tableConsumer, TabProtocol message);
	
}
