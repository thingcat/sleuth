package com.sleuth.block.store;

import com.sleuth.block.schema.Chain;

/** 链路状态信息
 * 
 * @author Jonse
 * @date 2021年5月14日
 */
public interface ChainStore {
	
	public abstract Chain get();
	
	public abstract void set(Long height, String blockHash);
	
}
