package com.sleuth.core.storage.datastore.setter;

import java.util.List;

import org.rocksdb.ColumnFamilyHandle;

/** 批量处理
 * 
 * @author Jonse
 * @date 2020年1月19日
 * @param <T>
 */
public interface BatchStoreParameterSetter {
	
	/** 批量数据处理
	 * 
	 * @param familyHandle
	 * @return
	 * @throws Exception
	 */
	public abstract List<BytesObject> setValues(ColumnFamilyHandle familyHandle) throws Exception;

}
