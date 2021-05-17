package com.sleuth.network.peer.hash;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import com.sleuth.network.peer.PeerNetwork;
import com.sleuth.network.schema.NodeInfo;


/**
 * @author zg
 * @description 客户端网络连接处理
 * @date 2018/8/21
 */
public class LocalDHTNode {
	
    private final BigInteger nid; // 节点id
    private final Stack<NodeInfo> newNodes; // 未请求或短时间内未请求的节点信息
    private final Set<String> oldNodes = new LinkedHashSet<String>(); // 已经请求过的节点信息, 共6个字符, 4个ip字节加2个端口字节进行iso-8859-1编码, 使用LinkedHashSet自动去重并保持顺序
    private final PeerNetwork peerNetwork; // 节点维护的路由列表

    public LocalDHTNode(BigInteger nid, Stack<NodeInfo> newNodes, PeerNetwork peerNetwork) {
        this.nid = nid;
        this.newNodes = newNodes;
        this.peerNetwork = peerNetwork;
    }

    public BigInteger getNid() {
        return nid;
    }

    public Stack<NodeInfo> getNewNodes() {
        return newNodes;
    }

    public Set<String> getOldNodes() {
        return oldNodes;
    }

	public PeerNetwork getNodeRouting() {
		return peerNetwork;
	}

}
