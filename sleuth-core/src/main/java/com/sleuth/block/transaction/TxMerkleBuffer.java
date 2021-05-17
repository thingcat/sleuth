package com.sleuth.block.transaction;

import com.sleuth.block.dto.TxMerkleDTO;

/** 默克尔树接收缓冲池
 * 
 * 1、等待默克尔树接收完整
 * 2、保存到正式库
 * 3、广播到其他节点
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public interface TxMerkleBuffer {
	
	/** 接收数据
	 * 
	 * @param merkle
	 * @return
	 */
	public abstract TxMerkleDTO recvfrom(TxMerkleDTO dto);
	
	/** 移除缓存
	 * 
	 * @param merkleHash
	 */
	public abstract void remove(String merkleHash);
	
}
