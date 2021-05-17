package com.sleuth.network.message;

import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.core.socket.message.Protocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;

/** 同步申请者
 * 
 * @author Jonse
 * @date 2020年11月16日
 */
public interface CmdApply {
	
	/** 申请发起同步
	 * 
	 * @param ch
	 */
	public abstract void onApply(CmdType cmd);
	
	/** 申请发起同步
	 * 
	 * @param ch
	 * @param data
	 */
	public abstract void onApply(CmdType cmd, Object data);
	
	/** 主动申请同步返回数据
	 * 
	 * @param manager
	 * @param protocol
	 */
	public abstract <T extends Protocol> void onTable(MessageManager manager, TabProtocol protocol);
}
