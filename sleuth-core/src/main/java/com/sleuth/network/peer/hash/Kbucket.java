package com.sleuth.network.peer.hash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sleuth.network.schema.NodeInfo;

/** 节点捅计算
 * 
 * @author Jonse
 * @date 2020年9月26日
 */
public class Kbucket {
	
    //桶起始的最小点，包含
    private BigInteger begin;
    //桶起始的结束点，不包含
    private BigInteger end;
    //桶里面包含的节点
    private List<NodeInfo> nodeInfos;

    public Kbucket() {
    	
    }
    
    public Kbucket(BigInteger begin, BigInteger end){
        this.begin = begin;
        this.end = end;
        this.nodeInfos = new ArrayList<NodeInfo>();
    }

    /**
     * 判断节点Id是否包含于桶内，是否可以加入到桶中
     * @param nid
     * @return
     */
    public boolean contains(BigInteger nid){
        boolean x = begin.compareTo(nid) <= 0;
        boolean y = end.compareTo(nid) > 0;
        return x && y;
    }

    /**
     * 添加节点到桶中
     * @param nodeInfo
     */
    public void addNode(NodeInfo nodeInfo){
    	//如果包含了该元素，如果没有包含，则可以加入
    	if (!this.nodeInfos.contains(nodeInfo)) {
    		this.nodeInfos.add(nodeInfo);
		}
    }

    /**
     * 获取桶内所有节点
     * @return
     */
    public Collection<NodeInfo> getNodes() {
        return this.nodeInfos;
    }

    /**
     * 清空桶内所有节点
     */
    public void clear() {
    	this.nodeInfos.clear();
    }

    /**
     * 桶裂变
     * @return
     */
    public Kbucket[] fissionKbucket(){

        //桶结束的位置减去桶开始的位置，如果小于8不能裂变
        BigInteger place = end.subtract(begin);
        //如果桶空间大小小于等于8，停止裂变
        if (place.compareTo(new BigInteger("8")) <= 0) {
        	return null;
        }
        //桶空间分裂为两部分
        BigInteger half = place.divide(new BigInteger("2"));
        //分裂出的新桶的起始位置(包含)，同时是旧桶的结束位置(不包含)
        BigInteger position = begin.add(half);
        //分裂出的两个全新的桶
        Kbucket k1 = new Kbucket(begin, position);
        Kbucket k2 = new Kbucket(position, end);
        //当前初始节点
        NodeInfo nodeInfo = (NodeInfo) this.nodeInfos.get(0);
        //分裂过程，保证自身节点永远在第一个裂变的桶中
        if (k1.contains(nodeInfo.getNid())){
            k1.addNode(nodeInfo);
            return new Kbucket[]{k1,k2};
        }else {
            k2.addNode(nodeInfo);
            return new Kbucket[]{k2,k1};
        }
    }

    public int compareTo(Kbucket o) {
        return begin.compareTo(o.begin);
    }

	public BigInteger getBegin() {
		return begin;
	}

	public void setBegin(BigInteger begin) {
		this.begin = begin;
	}

	public BigInteger getEnd() {
		return end;
	}

	public void setEnd(BigInteger end) {
		this.end = end;
	}
	
	public List<NodeInfo> getNodeInfos() {
		return nodeInfos;
	}

	public void setNodeInfos(List<NodeInfo> nodeInfos) {
		this.nodeInfos = nodeInfos;
	}
	
}
