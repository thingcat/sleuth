package com.sleuth.network.store;

import java.util.List;

import com.sleuth.network.schema.NodeInfo;

/** 节点保存
 * 
 * @author Jonse
 * @date 2020年9月25日
 */
public interface NodeInfoStore {

	/** 保存节点
	 * 
	 * @param nodeInfo
	 */
	public abstract void set(NodeInfo nodeInfo);
	
	/** 获得节点信息
	 * 
	 * @param key
	 * @return
	 */
	public abstract NodeInfo get(String key);
	
	/** 获取节点列表
	 * 
	 * @return
	 */
	public abstract List<NodeInfo> list();
	
}
