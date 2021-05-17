package com.sleuth.block;

import com.sleuth.block.schema.Block;

/** 区块的链路
 * <br/>
 * 
 * @author Jonse
 * @date 2021年1月28日
 */
public interface BlockChain {
	
	public abstract void onPreloading();
	
	/** 追加区块到链路
	 * 
	 * @param tappHash
	 * @param newBlock
	 */
	public abstract void addToChain(Block newBlock);
	
	/** 获取当前区块
	 * 
	 * @return
	 */
	public abstract Block currentBlock();
	
}
