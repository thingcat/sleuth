package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.Block;

/** 孤块存储
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public interface OrphanStore {
	
	/** 保存孤块
	 * 
	 * @param block
	 */
	public abstract void add(Block newBlock);
	
	/** 获得孤块
	 * 
	 * @param hash
	 */
	public abstract Block get(String hash);
	
	/** 获得所有的孤块
	 * 
	 * @return
	 */
	public abstract List<Block> list();
	
	/** 删除孤块
	 * 
	 * @param hash
	 */
	public abstract void remove(String hash);

}
