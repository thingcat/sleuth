package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.Block;

public interface BlockBufferStore {
	
	/** 保存区块
	 * 
	 * @param block
	 */
	public abstract void add(Block block);
	
	/** 获得区块
	 * 
	 * @param hash
	 */
	public abstract Block get(String hash);
	
	/** 移除
	 * 
	 * @param hash
	 */
	public abstract void remove(String hash);
	
	/** 获取所有的
	 * 
	 * @return
	 */
	public abstract List<Block> list();
	
}
