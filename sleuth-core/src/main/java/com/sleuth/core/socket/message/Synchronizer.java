package com.sleuth.core.socket.message;

import com.sleuth.network.message.protocol.TabProtocol;

/** 同步器
 * 
 * @author Jonse
 * @date 2021年2月7日
 */
public interface Synchronizer {
	
	/** 广播本地生产的数据
	 * @param <T>
	 * 
	 * @param schema
	 * @return
	 */
	public abstract <T> TabProtocol doProduce(T schema);
	
	/** 接收其他节点的消息，并转发到其他节点去
	 * @param <T>
	 * 
	 * @param dto
	 * @return
	 */
	public abstract <T extends DTO> TabProtocol doRecvFrom(T dto);
	
}
