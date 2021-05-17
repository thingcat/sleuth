package com.sleuth.block.transaction;

import java.util.List;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;

/** 事件接收缓冲池，等待被校验
 * 
 * @author Jonse
 * @date 2021年1月30日
 */
public interface TransactionBuffer {
	
	public abstract void onPreloading();
	
	/** 加入缓冲池
	 * 
	 * @param message
	 * @return
	 */
	public abstract void push(TransactionDTO tx);
	
	/** 打包交易
	 * 
	 * @param tappHash
	 * @return
	 */
	public abstract Transaction[] baleTransactions();
	
	/** 删除
	 * 
	 * @param action
	 */
	public abstract void remove(Transaction transaction);
	
	/** 缓冲池大小
	 * 
	 * @return
	 */
	public abstract int size();
	
	/** 获取缓冲池的事件
	 * 
	 * @param page
	 * @return
	 */
	public abstract List<Transaction> getTransactions(int page, int limit);

}
