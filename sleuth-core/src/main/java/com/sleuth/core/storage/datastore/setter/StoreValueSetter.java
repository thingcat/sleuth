package com.sleuth.core.storage.datastore.setter;

import org.rocksdb.ColumnFamilyHandle;

/** 对象存储参数处理
 * 
 * @author Jonse
 * @date 2020年8月23日
 * @param <T>
 */
public interface StoreValueSetter<T> {
	
	/** 反序列化
	 * @param <T>
	 * 
	 * @param familyHandle
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public abstract T setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception;
	
}
