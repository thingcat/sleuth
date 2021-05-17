package com.sleuth.network.message;

import com.sleuth.network.message.protocol.TabProtocol;

/** 消费者
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
public interface TableProtocolConsumer {
	
	/** 往队列里面添加数据
	 * 
	 * @param ep
	 */
	public abstract void push(TabProtocol message);
	
	/** 弹出队列数据
	 * 
	 * @return
	 */
	public abstract TabProtocol poll();
	
	/** 当前队列大小
	 * 
	 * @return
	 */
	public abstract long size();
	
	/** 清除ID
	 * 
	 */
	public abstract void clearIds();
	
	/** 加入消息ID
	 * 
	 * @param msgId
	 */
	public abstract void joinUidBuffer(String uid);
	
	/** 是否存在的消息ID
	 * 
	 * @param msgId
	 * @return
	 */
	public abstract boolean exists(String msgUid);
}
