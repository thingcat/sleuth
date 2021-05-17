package com.sleuth.api.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class NodeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8800729756273167339L;
	
	private BigInteger nid;
	private String lable;
	private String uri;//地址
	private Integer port;//端口
	private Integer weight;//权重，-1表示永远不连接

    public NodeDTO() {
    	
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
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
}
