package com.sleuth.block.transaction;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;

public interface TransactionManager {
	
	public abstract Transaction findById(String txId);
	
	/** 加入到缓存中，一般通过API接口产生的交易
	 * 
	 * @param result
	 */
	public abstract void addBuffer(TransactionDTO dto);
	
	/** 接收交易，其他节点同步过来的交易
	 * 
	 * @param dto
	 * @return
	 */
	public abstract void pushResult(TransactionDTO dto);
	
	/** 主动申请同步过来的交易，区块里面的交易
	 * 
	 * @param merkle
	 */
	public abstract void pullResult(TransactionDTO dto);
	
	/** 打包交易
	 * 
	 * @param actions
	 */
	public abstract Transaction[] baleBufferTransactions();
	
	/** 区块生成后，将缓存中的交易转移到区块中（正式库）
	 * 
	 * buffer -> block
	 * 
	 * @param actions
	 */
	public abstract void baleTransfer(Transaction[] transactions);
	
	/** 移除缓存中的交易
	 * 
	 * @param action
	 */
	public abstract void rmvBufferTransaction(Transaction transaction);
	
	public abstract TransactionService getService();
	
	public abstract TransactionBuffer getBuffer();
	
}
