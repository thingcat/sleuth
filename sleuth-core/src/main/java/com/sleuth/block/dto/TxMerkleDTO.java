package com.sleuth.block.dto;

import com.sleuth.core.socket.message.DTO;

/** 默克尔树传输对象
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public class TxMerkleDTO extends DTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5596466396709470906L;
	
	private String hash;//默克尔树hash值
	private String[] txIds;//交易IDs
	private Long createAt;
	private boolean hasNext;//是否有下一组，解决交易HASH值过多的影响
	
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	public boolean getHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String[] getTxIds() {
		return txIds;
	}
	public void setTxIds(String[] txIds) {
		this.txIds = txIds;
	}

}
