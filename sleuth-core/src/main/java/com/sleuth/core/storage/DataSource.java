package com.sleuth.core.storage;

import java.util.List;

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.Snapshot;
import org.rocksdb.Transaction;

public interface DataSource {
	
	/** 初始化数据库配置
	 * 
	 */
	public abstract void init();
	
	/** 打开数据库
	 * 
	 * @return
	 */
	public abstract RocksDB getRocksDB();
	
	/** 创建簇列
	 * 
	 * @param familyName
	 */
	public abstract ColumnFamilyHandle createFamily(String familyName);
	
	/** 获取所有簇列的描述符
	 * 
	 * @return
	 */
	public abstract List<ColumnFamilyDescriptor> getFamilyDescriptors();
	
	/** 获取所有簇列的操作句柄
	 * 
	 * @return
	 */
	public abstract List<ColumnFamilyHandle> getFamilyHandles();
	
	/** 获取簇列的操作句柄
	 * 
	 * @param familyName
	 * @return
	 */
	public abstract ColumnFamilyHandle getFamilyHandle(String familyName);
	
	/** 关闭数据库
	 * 
	 */
	public abstract void close();
	
	/** 获取数据路径
	 * 
	 * @return
	 */
	public abstract String getDbPath();
	
	/** 配置读属性
	 * 
	 * @return
	 */
	public abstract ReadOptions getReadOptions(Snapshot snapshot);
	
	/** 开启事务
	 * 
	 */
	public abstract void openTransaction();
	
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
