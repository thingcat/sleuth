package com.sleuth.network.dto;

import java.math.BigInteger;

import com.sleuth.core.socket.message.DTO;

public class NodeInfoDTO extends DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7438900229903146859L;

	private String label;//节点标识符号
	private BigInteger nid;
    private String uri;//地址
    private Integer weight;//权重，-1表示永远不连接
    
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
	
}
