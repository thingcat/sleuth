package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.Block;

public interface BlockStore {
	
	/** 保存区块
	 * 
	 * @param block
	 */
	public abstract void add(Block newBlock);
	
	/** 获得区块
	 * 
	 * @param hash
	 */
	public abstract Block get(String hash);
	
	/** 根据hash获得主链区块
	 * 
	 * @param hash
	 * @param limit 数量
	 * @return
	 */
	public abstract List<Block> getBlocks(String hash, int limit);
	
}
