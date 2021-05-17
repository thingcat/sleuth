package com.sleuth.block;

import com.sleuth.block.schema.Block;

/** 算力管理，控制在每隔5分钟出一个块
 * 
 * <p>全网算力GHP: Global Hash Power</p>
 * <p>节点算力NHP: Node Hash Power</p>
 * 
 * @author Jonse
 * @date 2021年3月18日
 */
public interface GlobalPower {
	
	/** 计算算力
	 * 
	 * @param newBlock
	 */
	public abstract void calculate(Block newBlock);
	
	/** 获得全网算力值
	 * 
	 * @return
	 */
	public abstract long ghpCountValue();
	
	/** 修改算力系数
	 * 
	 * @param tapp
	 * @return
	 */
	public abstract void update();
	
}
