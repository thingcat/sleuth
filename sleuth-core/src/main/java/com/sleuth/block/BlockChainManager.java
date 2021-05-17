package com.sleuth.block;

import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.mine.work.WorkResult;
import com.sleuth.block.schema.Block;

/** 区块链管理
 * 
 * @author Jonse
 * @date 2021年2月5日
 */
public interface BlockChainManager {
	
	/** 保存挖矿工作结果
	 * 
	 * @param result
	 * @return
	 */
	public abstract void addResult(WorkResult result);
	
	/** 接收其他节点产生的区块
	 * 
	 * @param protocol
	 * @return
	 */
	public abstract void pushResult(BlockDTO dto);
	
	/** 申请同步过来的数据
	 * 
	 * @param block
	 */
	public abstract void pullResult(BlockDTO dto);
	
	/** 校验区块的合法性
	 * 
	 * @param block
	 * @return
	 */
	public abstract boolean verify(BlockDTO block);
	
	/** 是否为孤块
	 * 
	 * @param block
	 * @return
	 */
	public abstract boolean isOrphanBlock(BlockDTO block);
	
	/** 添加到孤块池
	 * 
	 * @param block
	 */
	public abstract void addOrphanBlock(BlockDTO block);
	
	/** 验证默克尔树是否接收完成
	 * 
	 * @param block
	 * @return
	 */
	public abstract boolean checkMerkle(BlockDTO block);
	
	/** 获取区块
	 * 
	 * @param hash
	 * @return
	 */
	public abstract Block getBlock(String hash);
	
	/** 最新区块
	 * 
	 * @return
	 */
	public abstract Block currentBlock();
	
}
