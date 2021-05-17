package com.sleuth.block.mine.work;

import java.math.BigInteger;

/** 工作量计算结果
 * 
 * @author Jonse
 * @date 2018年11月1日
 */
public class PowResult {
	
	private Integer bitsRatio;
	private Long nonce;//计数器
	private String txRoot;//默克尔树
	private String wxRoot;//webURI树
	private String hash;//区块的hash值
	private BigInteger target;//目标难度值
	private long startTime;//开始时间
	private long endTime;//结束时间
	
	public PowResult(Integer bitsRatio, BigInteger target, long nonce, String hash) {
		this.bitsRatio = bitsRatio;
		this.target = target;
		this.nonce = nonce;
		this.hash = hash;
	}
	
	public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public BigInteger getTarget() {
		return target;
	}

	public void setTarget(BigInteger target) {
		this.target = target;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getTxRoot() {
		return txRoot;
	}

	public void setTxRoot(String txRoot) {
		this.txRoot = txRoot;
	}

	public String getWxRoot() {
		return wxRoot;
	}

	public void setWxRoot(String wxRoot) {
		this.wxRoot = wxRoot;
	}

	public Integer getBitsRatio() {
		return bitsRatio;
	}

	public void setBitsRatio(Integer bitsRatio) {
		this.bitsRatio = bitsRatio;
	}

}
