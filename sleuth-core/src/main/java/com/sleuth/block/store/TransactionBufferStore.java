package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.Transaction;

/** 广播池，接收来自其它节点的事件广播
 * 
 * @author Jonse
 * @date 2021年1月30日
 */
public interface TransactionBufferStore {
	
	/** 加入临时缓冲池
	 * 
	 * @param tx
	 */
	public abstract void add(Transaction tx);
	
	/** 获取一个事件
	 * 
	 * @param txId
	 * @return
	 */
	public abstract Transaction get(String txId);
	
	/** 获取当前所有的事件
	 * 
	 * @return
	 */
	public abstract List<Transaction> list();
	
	/** 删除事件
	 * 
	 * @param txId
	 */
	public abstract void delete(String txId);
	
	/** 删除一组事件
	 * 
	 * @param txIds
	 */
	public abstract void delete(String[] txIds);
	
}
