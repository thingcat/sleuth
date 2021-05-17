package com.sleuth.core.storage.cfg;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.rocksdb.util.SizeUnit;

/** 数据库属性配置
 * 
 * @author Jonse
 * @date 2020年1月13日
 */
public class Configurable {
	
	static final Map<String, String> property = new HashMap<String, String>();
	
	public static final String store_default_root = "store";
	
	public static final String store_root = "store.root";
	public static final String store_db = "store.db";
	public static final String store_createIfMissing = "store.createIfMissing";
	public static final String store_writeBufferSize = "store.writeBufferSize";
	public static final String store_maxWriteBufferNumber = "store.maxWriteBufferNumber";
	public static final String store_maxBackgroundCompactions = "store.maxBackgroundCompactions";
	
	public static final String store_root_value = System.getProperty("user.dir") + "/" + store_default_root;
	public static final String store_db_value = "0";
	public static final String store_createIfMissing_value = "true";
	public static final String store_writeBufferSize_value = String.valueOf(8 * SizeUnit.KB);
	public static final String store_maxWriteBufferNumber_value = "4";
	public static final String store_maxBackgroundCompactions_value = "4";
	
	//默认初始化配置
	static {
		//数据库存放位置
		property.put(store_root, store_root_value);
		//选择的库
		property.put(store_db, store_db_value);
		property.put(store_createIfMissing, store_createIfMissing_value);
		property.put(store_writeBufferSize, store_writeBufferSize_value);
		property.put(store_maxWriteBufferNumber, store_maxWriteBufferNumber_value);
		property.put(store_maxBackgroundCompactions, store_maxBackgroundCompactions_value);
	}
	
	public Configurable() {
		
	}
	
	public Configurable(Properties properties) {
		Set<String> keys = property.keySet();
		for (String key : keys) {
			Object value = properties.get(key);
			if (value != null && !"".equals(value)) {
				property.put(key, String.valueOf(value));
			}
		}
	}
	
}
