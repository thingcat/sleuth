package com.sleuth.api.dto;

import java.io.Serializable;
import java.math.BigInteger;

import com.sleuth.core.utils.DateUtils;

/**
 * 区块
 * 
 * @author Jonse
 * @date 2018年10月31日
 */
public class BlockDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1693114862233535639L;
	
	private String hash;// 区块hash值
	private String tappHash;//应用hash地址
	private String prevBlockHash;// 前一个区块的hash值
	private String merkleHash;//默克尔树根节点
	private String data;// 数据
	private Long startTime;//开始时间
	private Long endTime;//结束时间
	private Long createAt;// 区块创建时间(单位:秒)
	private BigInteger target;//目标难度值
	private Long nonce;//工作量证明计数器
	private Long height;//区块高度
	private int acts;//交易元数量
	private String bc;//播报方-Broadcast
	
	public BlockDTO() {
		
	}
	
	public BlockDTO(String tappHash, String prevBlockHash, long height) {
		this.tappHash = tappHash;
		this.prevBlockHash = prevBlockHash;
		this.data = "";
		this.height = height;
		this.createAt = DateUtils.nowToUtc();
	}

	public BlockDTO(String tappHash, String prevBlockHash, long height, String data) {
		this.tappHash = tappHash;
		this.prevBlockHash = prevBlockHash;
		if (data == null) {
			data = "";
		}
		this.data = data;
		this.height = height;
		this.createAt = DateUtils.nowToUtc();
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTappHash() {
		return tappHash;
	}

	public void setTappHash(String tappHash) {
		this.tappHash = tappHash;
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

	public void setHeight(Long height) {
		this.height = height;
	}

	public long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public String getMerkleHash() {
		return merkleHash;
	}

	public void setMerkleHash(String merkleHash) {
		this.merkleHash = merkleHash;
	}

	public Long getHeight() {
		return height;
	}

	public BigInteger getTarget() {
		return target;
	}

	public void setTarget(BigInteger target) {
		this.target = target;
	}

	public int getActs() {
		return acts;
	}

	public void setActs(int acts) {
		this.acts = acts;
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

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockDTO) {
			BlockDTO block = (BlockDTO) obj;
			return this.getHash().equals(block.getHash());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Block [hash=" + hash + ", tappHash=" + tappHash + ", prevBlockHash=" + prevBlockHash + ", merkleHash="
				+ merkleHash + ", data=" + data + ", createAt=" + createAt + ", target=" + target + ", nonce=" + nonce
				+ ", height=" + height + "]";
	}

}
