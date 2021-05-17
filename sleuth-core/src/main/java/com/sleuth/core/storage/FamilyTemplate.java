package com.sleuth.core.storage;

import java.util.List;

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;

/** 携带一个ColumnFamilyHandle指针来声明你希望写到哪个列族
 * 
 * @author Jonse
 * @date 2020年8月23日
 */
public interface FamilyTemplate {
	
	/** 创建一个列族描述符
	 * 
	 * @param familyName
	 */
	public abstract ColumnFamilyHandle createFamily(String familyName);
	
	/** 获取列族描述符 列表
	 * 
	 * @return
	 */
	public abstract List<ColumnFamilyDescriptor> getColumnFamilies();
	
	
	/** 寻找簇列
	 * 
	 * @param familyName
	 * @return
	 */
	public abstract ColumnFamilyHandle findFamily(String familyName);
	
	/** 是否存在相同的簇列
	 * 
	 * @param familyName
	 * @return
	 */
	public abstract boolean exists(String familyName);
	
}
