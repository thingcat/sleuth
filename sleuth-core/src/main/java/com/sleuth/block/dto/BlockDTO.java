package com.sleuth.block.dto;

import java.math.BigInteger;

import com.sleuth.core.socket.message.DTO;

public class BlockDTO extends DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4650098031879462271L;

	private String hash;// 区块hash值
	private String prevBlockHash;// 前一个区块的hash值
	private String wxRoot;//WebURIRoot根节点
	private String txRoot;//交易订单默克尔树根节点
	private String data;// 数据
	
	private Long startTime;//开始时间
	private Long endTime;//结束时间
	
	private Integer bitsRatio;//目标难度系数
	private BigInteger target;//目标难度值
	private Long nonce;//工作量证明计数器
	private Long height;//区块高度
	
	private Long createAt;// 区块创建时间(单位:秒)
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getPrevBlockHash() {
		return prevBlockHash;
	}
	public void setPrevBlockHash(String prevBlockHash) {
		this.prevBlockHash = prevBlockHash;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	public Long getNonce() {
		return nonce;
	}
	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}
	public Long getHeight() {
		return height;
	}
	public void setHeight(Long height) {
		this.height = height;
	}
	public BigInteger getTarget() {
		return target;
	}
	public void setTarget(BigInteger target) {
		this.target = target;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getWxRoot() {
		return wxRoot;
	}
	public void setWxRoot(String wxRoot) {
		this.wxRoot = wxRoot;
	}
	public String getTxRoot() {
		return txRoot;
	}
	public void setTxRoot(String txRoot) {
		this.txRoot = txRoot;
	}
	public Integer getBitsRatio() {
		return bitsRatio;
	}
	public void setBitsRatio(Integer bitsRatio) {
		this.bitsRatio = bitsRatio;
	}
	
}
