package com.sleuth.block.store;

import com.sleuth.block.schema.Transaction;

/** 保存区块里面的交易
 * 
 * @author Jonse
 * @date 2020年9月19日
 */
public interface TransactionStore {
	
	/** 保存交易
	 * 
	 * @param tx
	 */
	public abstract void add(Transaction tx);
	
	/** 保存交易
	 * 
	 * @param transactions
	 */
	public abstract void add(Transaction[] transactions);
	
	/** 获取交易
	 * 
	 * @param axId
	 * @return
	 */
	public abstract Transaction get(String axId);
	
}
