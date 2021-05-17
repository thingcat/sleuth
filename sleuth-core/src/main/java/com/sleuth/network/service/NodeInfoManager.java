package com.sleuth.network.service;

import com.sleuth.network.dto.NodeInfoDTO;

public interface NodeInfoManager {
	
	/** 保存节点
	 * 
	 * @param name
	 * @param icon
	 */
	public abstract void add(NodeInfoDTO dto);
	
	/** 接收事件，其他节点同步过来的节点
	 * 
	 * @param dto
	 * @return
	 */
	public abstract void pushResult(NodeInfoDTO dto);
	
	/** 申请同步过来的事件，区块里面的节点
	 * 
	 * @param merkle
	 */
	public abstract void pullResult(NodeInfoDTO dto);

}
