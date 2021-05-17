package com.sleuth.core.storage.datastore;

import org.rocksdb.RocksDBException;
import org.rocksdb.Transaction;
import org.rocksdb.TransactionDB;
import org.rocksdb.WriteOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.storage.RocksTransaction;

public class ThingRocksTransaction implements RocksTransaction {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	private final WriteOptions writeOptions;
	private TransactionDB rocksDB;
	private Transaction transaction;
	
	public ThingRocksTransaction(TransactionDB rocksDB) {
		this.rocksDB = rocksDB;
		this.writeOptions = new WriteOptions();
		this.loadWriteOptions();
		this.transaction = this.rocksDB.beginTransaction(this.writeOptions);
	}
	
	@Override
	public Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	public boolean isTransaction() {
		return this.transaction != null;
	}

	@Override
	public void commit() {
		try {
			this.transaction.commit();
		} catch (RocksDBException e) {
			logger.error("transaction commit error, ", e);
		} finally {
			this.transaction = null;
		}
	}

	@Override
	public void rollback() {
		try {
			this.transaction.rollback();
		} catch (RocksDBException e) {
			logger.error("transaction rollback error, ", e);
		} finally {
			this.transaction = null;
		}
	}
	
	/** 加载事务写策略配置
	 * 
	 * @return
	 */
	private WriteOptions loadWriteOptions() {
		//如果为true，写入操作将不会首先进入预写日志，并且在崩溃后写入操作可能会丢失。
		this.writeOptions.setDisableWAL(false);
		//如果为true，并且用户试图写入不存在的列族（它们已被删除），则忽略写入（不返回错误）。如果一个WriteBatch中有多个写操作，则其他写操作将成功。默认值：false
		this.writeOptions.setIgnoreMissingColumnFamilies(false);
		//如果为true，我们需要等待或休眠写入请求，则会立即失败
		this.writeOptions.setNoSlowdown(false);
		//是否同步写入，同步效率低，异步效率高
		this.writeOptions.setSync(false);
		return this.writeOptions;
	}

}
