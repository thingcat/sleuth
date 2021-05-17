package com.sleuth.block.store;

import com.sleuth.block.schema.WxMerkle;

public interface WxMerkleStore {
	
	/** 保存默克尔树
	 * 
	 * <p>默克尔树只保存事件的ID值，事件的详细信息需要到事件表里面去找</p>
	 * 
	 */
	public abstract void add(WxMerkle merkle);
	
	/** 根据默克尔树根节点信息找到该节点下的所有事件的ID
	 * 
	 * @param merkleRoot
	 * @return
	 */
	public abstract WxMerkle get(String merkleRoot);
	
}
