package com.sleuth.core.storage;

import org.rocksdb.Transaction;

/** 事务管理
 * 
 * @author Jonse
 * @date 2021年2月3日
 */
public interface RocksTransaction {
	
	/** 获取事务对象
	 * 
	 * @return
	 */
	public abstract Transaction getTransaction();
	
	/** 是否开启了事务
	 * 
	 * @return
	 */
	public abstract boolean isTransaction();
	
	/** 提交事务
	 * 
	 */
	public abstract void commit();
	
	/** 回滚事务
	 * 
	 */
	public abstract void rollback();

}
