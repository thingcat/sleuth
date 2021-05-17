package com.sleuth.network.service;

import java.util.List;

import com.sleuth.network.dto.NodeInfoDTO;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.schema.NodeInfo;

public interface NodeInfoService {
	
	/** 获取列表
	 * 
	 * @return
	 */
	public abstract List<NodeInfo> list();
	
	/** 保存数据
	 * 
	 * @param nodeInfo
	 */
	public abstract void add(NodeInfo nodeInfo);
	
	/** 广播本地生产的TAPP
	 * 
	 * @param block
	 * @return
	 */
	public abstract TabProtocol doProduce(NodeInfo nodeInfo);
	
	/** 广播接收到的TAPP
	 * 
	 * @param dto
	 * @return
	 */
	public TabProtocol doRecvFrom(NodeInfoDTO dto);
}
	
