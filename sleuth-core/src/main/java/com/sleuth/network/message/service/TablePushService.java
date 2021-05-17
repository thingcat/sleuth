package com.sleuth.network.message.service;

import com.sleuth.network.message.protocol.TabProtocol;

/** 数据推送
 * 
 * @author Jonse
 * @date 2021年1月19日
 */
public interface TablePushService {

	/** 实时推送过来的数据
	 * 
	 * @param message
	 * @return
	 */
	public abstract TabProtocol pushResult(TabProtocol message);
	
	/** 主动申请同步过来的数据
	 * 
	 * @param message
	 * @return
	 */
	public abstract void pullResult(TabProtocol message);
	
}
