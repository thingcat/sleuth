package com.sleuth.network.peer.hash;

import java.io.Serializable;

import com.sleuth.network.schema.NodeInfo;

/** 最近连接的节点
 * 
 * @author Jonse
 * @date 2018年11月16日
 */
public class RecentNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7519198260219485604L;

	private NodeInfo nodeInfo;//节点
	private Long createAt;//最后连接的时间
	
	public RecentNode(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
		this.createAt = System.currentTimeMillis();
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	
}
