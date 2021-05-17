package com.sleuth.block.transaction;

import com.sleuth.block.dto.TransactionDTO;

/** 交易验证池，推送过来的交易等待验证
 * 
 * <p>1、验证签名</p>
 * <p>1、验证时间戳</p>
 * 
 * @author Jonse
 * @date 2021年1月30日
 */
public interface TransactionVerify {
	
	/** 验证交易
	 * 
	 * @param dto
	 * @return
	 */
	public abstract boolean verify(TransactionDTO dto);
	
}
