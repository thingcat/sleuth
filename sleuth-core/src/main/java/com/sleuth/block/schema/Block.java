package com.sleuth.block.schema;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.DateUtils;

/**
 * 区块
 * 
 * @author Jonse
 * @date 2018年10月31日
 */
@Family(name="block")
public class Block extends BlockSchema {

	private String hash;// 区块hash值
	private String prevBlockHash;// 前一个区块的hash值
	private Long height;//区块高度
	
	private Integer bitsRatio;//目标难度系数
	private BigInteger target;//目标难度值
	private Long nonce;//工作量证明计数器
	
	private Long startTime;//开始时间
	private Long endTime;//结束时间
	
	private String wxRoot;//WebURIRoot根节点
	private String txRoot;//交易订单默克尔树根节点
	
	private BigDecimal fees;//手续费
	
	private String data;// 数据
	private Long createAt;// 区块创建时间(单位:秒)
	
	public Block() {
		
	}
	
	public Block(String prevBlockHash, long height) {
		this.prevBlockHash = prevBlockHash;
		this.data = "";
		this.height = height;
		this.createAt = DateUtils.nowToUtc();
	}

	public Block(String prevBlockHash, long height, String data) {
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

	public Long getHeight() {
		return height;
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

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Block) {
			Block block = (Block) obj;
			return this.getHash().equals(block.getHash());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Block [hash=" + hash + ", prevBlockHash=" + prevBlockHash + ", txRoot="
				+ txRoot + ", data=" + data + ", createAt=" + createAt + ", target=" + target + ", nonce=" + nonce
				+ ", height=" + height + "]";
	}

	@Override
	public String toUnique() {
		return this.hash;
	}
	
}
