package com.sleuth.network.message;

import com.sleuth.network.message.protocol.TabProtocol;

/** 数据协议处理
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
public interface TableProtocolHandler {
	
	/** 数据协议处理
	 * 
	 * @param message
	 */
	public abstract void handle(TableProtocolConsumer tableConsumer, TabProtocol message);
	
}
