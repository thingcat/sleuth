package com.sleuth.block.store;

import com.sleuth.block.schema.Transaction;

/** 保存区块里面的事件
 * 
 * @author Jonse
 * @date 2020年9月19日
 */
public interface TransactionStore {
	
	/** 保存事件
	 * 
	 * @param tx
	 */
	public abstract void add(Transaction tx);
	
	/** 保存事件
	 * 
	 * @param transactions
	 */
	public abstract void add(Transaction[] transactions);
	
	/** 获取事件
	 * 
	 * @param axId
	 * @return
	 */
	public abstract Transaction get(String axId);
	
}
