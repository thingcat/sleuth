package com.sleuth.network.schema;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

import com.sleuth.api.dto.NodeDTO;
import com.sleuth.core.storage.annotation.Family;
import com.sleuth.network.peer.hash.NodeId;

/**
 * 节点信息
 * 
 * @author Jonse
 * @date 2019年1月24日
 */
@Family(name="nodeinfo")
public class NodeInfo extends NodeInfoSchema {
	
	private String lable;
	private BigInteger nid;
    private String uri;//地址
    private Integer weight;//权重，-1表示永远不连接

    public NodeInfo() {
    	
    }
    
    public NodeInfo(NodeDTO node) {
    	this.nid = new NodeId().getNid();
    	this.uri = node.getUri();
    	this.lable = node.getLable();
    }
    
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public BigInteger getNid() {
		return nid;
	}
	public void setNid(BigInteger nid) {
		this.nid = nid;
	}
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getWeight() {
		if (this.weight == null) {
			return 0;
		}
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NodeInfo) {
			NodeInfo nodeInfo = (NodeInfo) obj;
			if (this == obj || nodeInfo.getUri().equals(this.getUri())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toUnique() {
		return DigestUtils.md5Hex(this.uri);
	}
	
	public URI toUri() {
		try {
			return new URI(this.uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error uri, " + this.uri);
		}
	}

	@Override
	public String toString() {
		return "NodeInfo [lable=" + lable + ", nid=" + nid + ", uri=" + uri + ", weight=" + weight + "]";
	}
	
}
