package com.sleuth.network.message;

import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.core.socket.message.Protocol;
import com.sleuth.network.message.protocol.TabProtocol;

/** 处理接收到的数据
 * 
 * <p>{"event":"table", "ch":"block/sync", "data":"json"}</p>
 * 
 * @author Jonse
 * @date 2020年11月18日
 */
public interface TableReceive {
	
	
	/** 广播过来的数据
	 * 
	 * @param protocol
	 */
	public abstract <T extends Protocol> void onTable(MessageManager manager, TabProtocol message);
	
}
