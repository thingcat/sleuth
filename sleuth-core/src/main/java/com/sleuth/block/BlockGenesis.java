package com.sleuth.block;

import com.sleuth.block.schema.Block;

/** 创世区块
 * 
 * @author Jonse
 * @date 2021年1月28日
 */
public interface BlockGenesis {
	
	/** 创世区块的构建
	 * 
	 * @return
	 */
	public abstract void onPreloading();
	
	/** 获取创世区块信息
	 * 
	 * @return
	 */
	public abstract Block get();
	
}
