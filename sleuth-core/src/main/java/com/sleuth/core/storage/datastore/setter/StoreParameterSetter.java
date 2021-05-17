package com.sleuth.core.storage.datastore.setter;

import org.rocksdb.ColumnFamilyHandle;

/** 对象存储参数处理
 * 
 * @author Jonse
 * @date 2020年8月23日
 * @param <T>
 */
public interface StoreParameterSetter {
	
	/** 设置参数
	 * 
	 * @param object
	 * @param db
	 * @param familyHandle
	 * @return
	 */
	public abstract BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception;
	
}
