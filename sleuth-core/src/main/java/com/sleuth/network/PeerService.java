package com.sleuth.network;

import java.util.List;

import com.sleuth.network.schema.NodeInfo;

/** 节点网络服务
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public interface PeerService {
	
	/** 从主网下载节点
	 * 
	 * @return
	 */
	public abstract List<NodeInfo> download();
	
	/** 启动网络服务
	 * 
	 */
	public abstract void start();
	
	/** 销毁网络服务
	 * 
	 */
	public abstract void destroy();
	
}
