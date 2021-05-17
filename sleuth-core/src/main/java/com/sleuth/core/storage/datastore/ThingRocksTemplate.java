package com.sleuth.core.storage.datastore;

import java.util.List;
import java.util.Set;

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.storage.DataSource;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.FamilyTemplate;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BatchStoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

/** 对象数据库
 * 
 * @author Jonse
 * @date 2020年1月18日
 */
public class ThingRocksTemplate extends AbstractRocksTemplate implements FamilyTemplate, RocksTemplate {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataSource dataSource;
	
	static {
		RocksDB.loadLibrary();
	}
	
	public ThingRocksTemplate() {
		
	}
	
	@Override
	public void beginTransaction() {
		this.dataSource.openTransaction();
	}

	@Override
	public void commit() {
		this.dataSource.commit();
	}
	
	@Override
	public void rollback() {
		this.dataSource.rollback();
	}
	
	@Override
	public void set(String familyName, String key, String value) {
		this.set(this.dataSource, familyName, key, value);
	}
	
	@Override
	public <T extends ProtostrffSchema> void set(String familyName, String key, T value, StoreParameterSetter storeParameterSetter) {
		if (!this.exists(familyName)) {
			this.createFamily(familyName);
		}
		this.set(this.dataSource, familyName, key, value, storeParameterSetter);
	}

	@Override
	public <T extends ProtostrffSchema> void set(Class<T> valueClass, StoreParameterSetter storeParameterSetter) {
		String familyName = this.findFamilyName(valueClass);
		if (!this.exists(familyName)) {
			this.createFamily(familyName);
		}
		this.set(this.dataSource, valueClass, storeParameterSetter);
	}
	
	@Override
	public <T extends ProtostrffSchema> void set(String familyName, BatchStoreParameterSetter batchStoreParameterSetter) {
		if (!this.exists(familyName)) {
			this.createFamily(familyName);
		}
		this.set(this.dataSource, familyName, batchStoreParameterSetter);
	}
	
	/** 保存数据
	 * 
	 * @param familyName
	 * @param key
	 * @param value
	 * @param storeParameterSetter
	 */
	@Override
	public <T extends ProtostrffSchema> void set(String familyName, StoreParameterSetter storeParameterSetter) {
		if (!this.exists(familyName)) {
			this.createFamily(familyName);
		}
		this.set(this.dataSource, familyName, storeParameterSetter);
	}
	

	@Override
	public <T extends ProtostrffSchema> void set(Class<T> valueClass, BatchStoreParameterSetter batchStoreParameterSetter) {
		String familyName = this.findFamilyName(valueClass);
		if (!this.exists(familyName)) {
			this.createFamily(familyName);
		}
		this.set(this.dataSource, valueClass, batchStoreParameterSetter);
	}
	
	@Override
	public <T extends ProtostrffSchema> T get(Class<T> valueClass, String key, StoreValueSetter<T> storeValueSetter) {
		return this.get(this.dataSource, valueClass, key, storeValueSetter);
	}
	
	@Override
	public <T extends ProtostrffSchema> T get(String familyName, String key, StoreValueSetter<T> storeValueSetter) {
		return this.get(this.dataSource, familyName, key, storeValueSetter);
	}

	@Override
	public <T extends ProtostrffSchema> List<T> forEach(Class<T> clazz, EachResult<T> iterator) {
		return this.forEach(this.dataSource, clazz, iterator);
	}
	
	@Override
	public <T extends ProtostrffSchema> List<T> forEach(String familyName, EachResult<T> iterator) {
		return this.forEach(this.dataSource, familyName, iterator);
	}
	
	@Override
	public <T extends ProtostrffSchema> List<T> forEach(int page, int limit, Class<T> clazz, EachResult<T> iterator) {
		int fromIndex = (page - 1) * limit;
		int toIndex = fromIndex + limit;
		List<T> list = this.forEach(clazz, iterator);
		if (list != null && list.size() > 0) {
			return list.subList(fromIndex, toIndex);
		}
		return null;
	}

	@Override
	public <T extends ProtostrffSchema> List<T> forEach(int page, int limit, String familyName,
			EachResult<T> iterator) {
		int fromIndex = (page - 1) * limit;
		int toIndex = fromIndex + limit;
		List<T> list = this.forEach(familyName, iterator);
		if (list != null && list.size() > 0) {
			return list.subList(fromIndex, toIndex);
		}
		return null;
	}
	
	@Override
	public List<ColumnFamilyDescriptor> getColumnFamilies() {
		return this.dataSource.getFamilyDescriptors();
	}
	
	@Override
	public <T extends ProtostrffSchema> void delete(Class<T> clazz, String key) {
		String familyName = this.findFamilyName(clazz);
		if (familyName != null) {
			this.delete(this.dataSource, familyName, key);
		}
	}
	
	@Override
	public <T extends ProtostrffSchema> void delete(Class<T> clazz, Set<String> keys) {
		String familyName = this.findFamilyName(clazz);
		if (familyName != null) {
			this.delete(this.dataSource, familyName, keys);
		}
	}
	
	@Override
	public void delete(String familyName, String key) {
		this.delete(this.dataSource, familyName, key);
	}

	@Override
	public void delete(String familyName, Set<String> keys) {
		this.delete(this.dataSource, familyName, keys);
	}
	
	@Override
	public ColumnFamilyHandle createFamily(String familyName) {
		return this.dataSource.createFamily(familyName);
	}
	
	@Override
	public ColumnFamilyHandle findFamily(String familyName) {
		return this.dataSource.getFamilyHandle(familyName);
	}

	@Override
	public boolean exists(String familyName) {
		ColumnFamilyHandle familyHandle = this.findFamily(familyName);
		return familyHandle == null;
	}
	
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
