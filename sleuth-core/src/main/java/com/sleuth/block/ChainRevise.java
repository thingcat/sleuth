package com.sleuth.block;

import com.sleuth.block.schema.Block;

/** 主链修正
 * 
 * @author Jonse
 * @date 2021年2月4日
 */
public interface ChainRevise  {
	
	/** 主链修正
	 * 
	 * @param tappHash
	 * @param block
	 */
	public abstract void revise(Block block);
	
}
