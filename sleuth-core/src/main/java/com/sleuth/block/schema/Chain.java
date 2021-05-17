package com.sleuth.block.schema;

import com.sleuth.core.storage.annotation.Family;

/** 当前链路状态
 * 
 * @author Jonse
 * @date 2020年9月22日
 */
@Family(name="chain")
public class Chain extends ChainSchema {
	
	private Long height;//当前区块的高度
	private String blockHash;//主链hash值
	
	public Chain() {
		
	}
	
	public Chain(Long height, String blockHash) {
		this.height = height;
		this.blockHash = blockHash;
	}
	
	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	@Override
	public String toUnique() {
		return this.height.toString();
	}

	@Override
	public String toString() {
		return "Chain [height=" + height + ", blockHash=" + blockHash + "]";
	}
	
}
