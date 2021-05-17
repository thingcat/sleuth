package com.sleuth.block.transaction;

import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.dto.WxMerkleDTO;

/** 默克尔树校验
 * 
 * <p>1、验证签名</p>
 * <p>2、验证默克尔树根节点是否匹配</p>
 * 
 * @author Jonse
 * @date 2021年1月30日
 */
public interface MerkleVerify {
	
	/** 验证默克尔树
	 * 
	 * @param action
	 * @return
	 */
	public abstract boolean verify(TxMerkleDTO merkle);
	
	/** 验证默克尔树
	 * 
	 * @param merkle
	 * @return
	 */
	public abstract boolean verify(WxMerkleDTO merkle);
	
}
