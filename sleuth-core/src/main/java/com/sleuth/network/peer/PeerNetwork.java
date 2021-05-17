package com.sleuth.network.peer;

import java.util.Collection;
import java.util.List;

import com.sleuth.network.peer.hash.Kbucket;
import com.sleuth.network.peer.hash.NodeId;
import com.sleuth.network.schema.NodeInfo;

/** P2P网络模型
 * 
 * @author Jonse
 * @date 2020年9月26日
 */
public interface PeerNetwork {
	
	/** 初始化网络模型
	 * 
	 */
	public abstract void init();
	
	/** 寻找附近的节点
     * 
     * @param nodeId
     * @return
     */
    public abstract List<NodeInfo> findNearNodes(NodeId nodeId);
    
    /**
     * 添加节点到路由表
     * @param node 需要添加的节点
     * @return
     */
    public abstract void addNode(NodeInfo node);
    
    /**
     * 添加节点到路由表
     * @param nodes 需要添加的节点
     * @return
     */
    public abstract void addNodes(Collection<NodeInfo> nodes);
	
    /** 获取所有的节点
     * 
     * @return
     */
    public abstract List<Kbucket> getBuckets();
	
    /** 获得本地节点在网络模型中的信息
     * 
     * @return
     */
    public abstract NodeInfo getLocalNodeInfo();
}
