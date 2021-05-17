package com.sleuth.core.storage;

import java.util.List;
import java.util.Set;

import com.sleuth.core.storage.datastore.setter.BatchStoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

/** 数据存储
 * 
 * @author Jonse
 * @date 2020年1月13日
 */
public interface RocksTemplate {
	
	/** 保存数据
	 * 
	 * @param valueClass
	 * @param storeParameterSetter
	 */
	public abstract <T extends ProtostrffSchema> void set(Class<T> valueClass, StoreParameterSetter storeParameterSetter);
	
	/** 保存数据
	 * 
	 * @param familyName
	 * @param key
	 * @param value
	 */
	public abstract void set(String familyName, String key, String value);
	
	/** 获取数据
	 * 
	 * @param valueClass
	 * @param key
	 * @param storeValueSetter
	 * @return
	 */
	public abstract <T extends ProtostrffSchema> T get(Class<T> valueClass, String key, StoreValueSetter<T> storeValueSetter);
	
	/** 获取数据
	 * 
	 * @param valueClass
	 * @param key
	 * @param storeValueSetter
	 * @return
	 */
	public abstract <T extends ProtostrffSchema> T get(String familyName, String key, StoreValueSetter<T> storeValueSetter);
	
	/** 保存数据
	 * 
	 * @param familyName
	 * @param key
	 * @param value
	 * @param storeParameterSetter
	 */
	public abstract <T extends ProtostrffSchema> void set(String familyName, String key, T value, StoreParameterSetter storeParameterSetter);
	
	/** 保存数据
	 * 
	 * @param familyName
	 * @param key
	 * @param value
	 * @param storeParameterSetter
	 */
	public abstract <T extends ProtostrffSchema> void set(String familyName, BatchStoreParameterSetter storeHandle);
	
	/** 保存数据
	 * 
	 * @param familyName
	 * @param storeParameterSetter
	 */
	public abstract <T extends ProtostrffSchema> void set(String familyName, StoreParameterSetter storeParameterSetter);
	
	/** 批量保存数据
	 * 
	 * @param familyName
	 * @param values
	 * @param storeHandle
	 */
	public abstract <T extends ProtostrffSchema> void set(Class<T> valueClass, BatchStoreParameterSetter storeHandle);
	
	/** 迭代
	 * 
	 * @param familyName
	 * @param clazz
	 * @param iterator
	 */
	public abstract <T extends ProtostrffSchema> List<T> forEach(Class<T> clazz, EachResult<T> iterator);
	
	/** 分页
	 * 
	 * @param page
	 * @param limit
	 * @param clazz
	 * @param iterator
	 * @return
	 */
	public abstract <T extends ProtostrffSchema> List<T> forEach(int page, int limit, Class<T> clazz, EachResult<T> iterator);
	
	/** 迭代
	 * 
	 * @param familyName
	 * @param iterator
	 */
	public abstract <T extends ProtostrffSchema> List<T> forEach(String familyName, EachResult<T> iterator);
	
	/** 分页
	 * 
	 * @param page
	 * @param limit
	 * @param clazz
	 * @param iterator
	 * @return
	 */
	public abstract <T extends ProtostrffSchema> List<T> forEach(int page, int limit, String familyName, EachResult<T> iterator);
	
	/** 删除
	 * 
	 * @param clazz
	 * @param key
	 */
	public abstract <T extends ProtostrffSchema> void delete(Class<T> clazz, String key);
	
	/** 批量删除
	 * 
	 * @param clazz
	 * @param keys
	 */
	public abstract <T extends ProtostrffSchema> void delete(Class<T> clazz, Set<String> keys);
	
	/** 删除
	 * 
	 * @param familyName
	 * @param key
	 */
	public abstract void delete(String familyName, String key);
	
	/** 批量删除
	 * 
	 * @param familyName
	 * @param keys
	 */
	public abstract void delete(String familyName, Set<String> keys);
	
	/** 获得数据源
	 * 
	 * @return
	 */
	public abstract DataSource getDataSource();
	
	/** 开启事务
	 * 
	 */
	public abstract void beginTransaction();
	
	/** 提交事务
	 * 
	 */
	public abstract void commit();
	
	/** 回滚事务
	 * 
	 */
	public abstract void rollback();
	
}
