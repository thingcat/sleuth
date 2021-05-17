package com.sleuth.block.store;

import com.sleuth.block.schema.WxMerkle;

public interface WxMerkleStore {
	
	/** 保存默克尔树
	 * 
	 * <p>默克尔树只保存交易的ID值，交易的详细信息需要到交易表里面去找</p>
	 * 
	 */
	public abstract void add(WxMerkle merkle);
	
	/** 根据默克尔树根节点信息找到该节点下的所有交易的ID
	 * 
	 * @param merkleRoot
	 * @return
	 */
	public abstract WxMerkle get(String merkleRoot);
	
}
