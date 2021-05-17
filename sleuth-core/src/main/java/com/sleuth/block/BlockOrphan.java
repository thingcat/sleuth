package com.sleuth.block;

import java.util.List;

import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.schema.Block;

/** 孤块管理
 * 
 * @author Jonse
 * @date 2021年2月5日
 */
public interface BlockOrphan {
	
	/** 保存孤块到孤块池
	 * 
	 * @param block
	 */
	public abstract void addOrphanBlock(BlockDTO block);
	
	/** 检测是否为孤块
	 * 
	 * @param block
	 * @return
	 */
	public abstract boolean isOrphanBlock(BlockDTO block);
	
	/** 获取所有的孤块
	 * 
	 * @return
	 */
	public abstract List<Block> getOrphanBlocks();
	
	/** 从孤块池移除孤块
	 * 
	 * @param block
	 */
	public abstract void rmvOrphanBlock(Block block);
	
}
