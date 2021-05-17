package com.sleuth.core.storage.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.Snapshot;
import org.rocksdb.Status;
import org.rocksdb.Transaction;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.sleuth.core.storage.DataSource;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.storage.datastore.setter.BatchStoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.exception.BatchStoreException;
import com.sleuth.core.storage.exception.FamilyNotFoundException;
import com.sleuth.core.storage.exception.FamilyStoreException;
import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

public abstract class AbstractRocksTemplate {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 数据库操作
	 * 
	 * @author Jonse
	 * @date 2020年9月14日
	 */
	private abstract class RocksHandle {
		
		public RocksHandle(DataSource dataSource, Class<?> targetClass) {
			//寻找簇列
			String familyName = findFamilyName(targetClass);
			if (familyName == null) {
				throw new FamilyNotFoundException();
			}
			this.handle(dataSource, familyName);
		}
		
		public RocksHandle(DataSource dataSource, String familyName) {
			this.handle(dataSource, familyName);
		}
		
		/** 执行数据库操作
		 * 
		 * @param dataSource
		 * @param familyName
		 */
		public void handle(DataSource dataSource, String familyName) {
			ColumnFamilyHandle familyHandle = dataSource.getFamilyHandle(familyName);
			if (familyHandle == null) {
				familyHandle = dataSource.createFamily(familyName);
			}
			try {
				this.handle(dataSource, familyHandle);
			} catch (Exception e) {
				if (e instanceof RocksDBException) {
					RocksDBException rex = (RocksDBException) e;
					Status.Code errorCode = rex.getStatus().getCode();
					logger.error("error code : {}", errorCode.name());
				}
				if (dataSource.isTransaction()) {
					dataSource.rollback();//回滚事务
				}
				logger.error("db execute error, ", e);
				throw new FamilyStoreException(familyName);
			}
		}
		
		/** 执行读写操作任务
		 * 
		 * @param db
		 * @param familyHandle
		 * @return <T>
		 * @throws Exception
		 */
		public abstract void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception;
		
	}
	
	public void set(DataSource dataSource, String familyName, String key, String value) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws RocksDBException {
				logger.debug("find family - {}, save value.", familyName);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					db.put(familyHandle, key.getBytes(), value.getBytes());
				} else {
					RocksDB db = dataSource.getRocksDB();
					db.put(familyHandle, key.getBytes(), value.getBytes());
				}
			}
		};
	}
	
	public <T extends ProtostrffSchema> void set(DataSource dataSource, String familyName, String key, T value, StoreParameterSetter storeParameterSetter) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", familyName);
				BytesObject param = storeParameterSetter.setValue(familyHandle);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					db.put(familyHandle, key.getBytes(), param.getValue());
				} else {
					RocksDB db = dataSource.getRocksDB();
					db.put(familyHandle, param.getKey(), param.getValue());
				}
			}
		};
		
	}
	
	/** 保存对象
	 * 
	 * @param dbPath
	 * @param configurable
	 * @param familyName
	 * @param value
	 * @param storeHandle
	 */
	protected <T extends ProtostrffSchema> void set(DataSource dataSource, Class<T> targetClass, StoreParameterSetter storeParameterSetter) {
		new RocksHandle(dataSource, targetClass) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", new String(familyHandle.getName()));
				BytesObject param = storeParameterSetter.setValue(familyHandle);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					db.put(familyHandle, param.getKey(), param.getValue());
				} else {
					RocksDB db = dataSource.getRocksDB();
					db.put(familyHandle, param.getKey(), param.getValue());
				}
			}
		};
	}
	
	/** 保存对象
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param storeParameterSetter
	 */
	protected <T extends ProtostrffSchema> void set(DataSource dataSource, String familyName, StoreParameterSetter storeParameterSetter) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", new String(familyHandle.getName()));
				BytesObject param = storeParameterSetter.setValue(familyHandle);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					db.put(familyHandle, param.getKey(), param.getValue());
				} else {
					RocksDB db = dataSource.getRocksDB();
					db.put(familyHandle, param.getKey(), param.getValue());
				}
			}
		};
	}
	
	/** 保存集合
	 * 
	 * @param dbPath
	 * @param configurable
	 * @param family
	 * @param values
	 * @param storeHandle
	 */
	protected <T extends ProtostrffSchema> void set(DataSource dataSource, Class<T> targetClass, BatchStoreParameterSetter batchSetter) {
		new RocksHandle(dataSource, targetClass) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", new String(familyHandle.getName()));
				
				List<BytesObject> params = batchSetter.setValues(familyHandle);
				if (params != null && params.size() > 0) {
					if (dataSource.isTransaction()) {
						Transaction db = dataSource.getTransaction();
						for (BytesObject param : params) {
							db.put(familyHandle, param.getKey(), param.getValue());
						}
					} else {
						final WriteOptions writeOpt = new WriteOptions();
						writeOpt.setDisableWAL(false);
						writeOpt.setSync(true);
						final WriteBatch writeBatch = new WriteBatch();
						for (BytesObject param : params) {
							try {
								writeBatch.put(familyHandle, param.getKey(), param.getValue());
							} catch (Exception e) {
								writeOpt.close();
								writeBatch.close();
								throw new BatchStoreException(e);
							}
						}
						RocksDB db = dataSource.getRocksDB();
						db.write(writeOpt, writeBatch);
						writeOpt.close();
						writeBatch.close();
					}
				} 
			}
		};
		
	}
	
	/** 保存集合
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param batchSetter
	 */
	protected <T extends ProtostrffSchema> void set(DataSource dataSource, String familyName, BatchStoreParameterSetter batchSetter) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", new String(familyHandle.getName()));
				
				List<BytesObject> params = batchSetter.setValues(familyHandle);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					for (BytesObject param : params) {
						db.put(familyHandle, param.getKey(), param.getValue());
					}
				} else {
					final WriteOptions writeOpt = new WriteOptions();
					writeOpt.setDisableWAL(false);
					writeOpt.setSync(true);
					final WriteBatch writeBatch = new WriteBatch();
					try {
						if (params != null && params.size() > 0) {
							params.forEach(param->{
								try {
									writeBatch.put(familyHandle, param.getKey(), param.getValue());
								} catch (Exception e) {
									throw new BatchStoreException(e);
								}
							});
							RocksDB db = dataSource.getRocksDB();
							db.write(writeOpt, writeBatch);
						}
					} catch (Exception e) {
						logger.error("store batch error, ", e);
						throw new BatchStoreException(e);
					} finally {
						writeOpt.close();
						writeBatch.close();
					}
				}
			}
		};
		
	}
	
	/** 获得对象数据
	 * 
	 * @param dataSource
	 * @param targetClass
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ProtostrffSchema> T get(DataSource dataSource, Class<T> targetClass, String key, StoreValueSetter<T> storeValueSetter) {
		Object[] object = new Object[1];
		new RocksHandle(dataSource, targetClass) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", new String(familyHandle.getName()));
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					byte[] bytes = db.get(familyHandle, readOptions, key.getBytes());
					if (bytes != null) {
						object[0] = storeValueSetter.setValue(familyHandle, bytes);
					}
				} else {
					RocksDB db = dataSource.getRocksDB();
					byte[] bytes = db.get(familyHandle, key.getBytes());
					if (bytes != null) {
						object[0] = storeValueSetter.setValue(familyHandle, bytes);
					}
				}
			}
		};
		return (T) object[0];
	}
	
	/** 获得对象数据
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param key
	 * @param storeValueSetter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ProtostrffSchema> T get(DataSource dataSource, String familyName, String key, StoreValueSetter<T> storeValueSetter) {
		Object[] object = new Object[1];
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute storehandle.", familyName);
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					byte[] bytes = db.get(familyHandle, readOptions, key.getBytes());
					if (bytes != null) {
						object[0] = storeValueSetter.setValue(familyHandle, bytes);
					}
				} else {
					RocksDB db = dataSource.getRocksDB();
					byte[] bytes = db.get(familyHandle, key.getBytes());
					if (bytes != null) {
						object[0] = storeValueSetter.setValue(familyHandle, bytes);
					}
				}
			}
		};
		return (T) object[0];
	}
	
	/** 迭代数据
	 * 
	 * @param dbPath
	 * @param configurable
	 * @param familyName
	 * @param clazz
	 * @param iterator
	 */
	protected <T extends ProtostrffSchema> List<T> forEach(DataSource dataSource, Class<T> targetClass, EachResult<T> eachResult) {
		List<T> list = new ArrayList<T>();
		new RocksHandle(dataSource, targetClass) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute eachResult.", new String(familyHandle.getName()));
				RocksIterator iterator = null;
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					iterator = db.getIterator(readOptions);
					this.forEach(iterator, list);
					db.clearSnapshot();
				} else {
					RocksDB db = dataSource.getRocksDB();
					iterator = db.newIterator(familyHandle);
					this.forEach(iterator, list);
				}
			}
			
			private void forEach(RocksIterator iterator, List<T> list) {
				if (iterator != null) {
					for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
						T e = eachResult.valueOf(iterator.key(), iterator.value());
						if (e == null) {
							continue;
						}
						list.add(e);
					}
				}
			}
		};
		return list;
	}
	
	/** 迭代数据
	 * 
	 * @param dataSource
	 * @param targetClass
	 * @param targetKeyAfter 目标之后的
	 * @param eachResult
	 * @return
	 */
	protected <T extends ProtostrffSchema> List<T> forEach(DataSource dataSource, Class<T> targetClass, 
			String targetKeyAfter, int limit, EachResult<T> eachResult) {
		List<T> list = new ArrayList<T>();
		new RocksHandle(dataSource, targetClass) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute eachResult.", new String(familyHandle.getName()));
				RocksIterator iterator = null;
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					iterator = db.getIterator(readOptions);
					this.forEach(iterator, targetKeyAfter, list);
					db.clearSnapshot();
				} else {
					RocksDB db = dataSource.getRocksDB();
					iterator = db.newIterator(familyHandle);
					this.forEach(iterator, targetKeyAfter, list);
				}
			}
			
			private void forEach(RocksIterator iterator, String targetKeyAfter, List<T> list) {
				if (iterator != null) {
					if (targetKeyAfter != null && !targetKeyAfter.trim().equals("")) {
						iterator.seekForPrev(targetKeyAfter.getBytes());
					} else {
						iterator.seekToFirst();
					}
					int i = 0;
					for(; iterator.isValid(); iterator.next()) {
						T e = eachResult.valueOf(iterator.key(), iterator.value());
						if (e == null) {
							continue;
						}
						list.add(e);
						if (i > limit) {
							break;
						}
					}
				}
			}
		};
		return list;
	}
	
	/** 迭代数据
	 * 
	 * @param dbPath
	 * @param configurable
	 * @param familyName
	 * @param clazz
	 * @param iterator
	 */
	protected <T extends ProtostrffSchema> List<T> forEach(DataSource dataSource, String familyName, EachResult<T> eachResult) {
		List<T> list = Lists.newArrayList();
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute eachResult.", familyName);
				RocksIterator iterator = null;
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					iterator = db.getIterator(readOptions);
					this.forEach(iterator, list);
					db.clearSnapshot();
				} else {
					RocksDB db = dataSource.getRocksDB();
					iterator = db.newIterator(familyHandle);
					this.forEach(iterator, list);
				}
			}
			
			private void forEach(RocksIterator iterator, List<T> list) {
				if (iterator != null) {
					for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
						T e = eachResult.valueOf(iterator.key(), iterator.value());
						if (e == null) {
							continue;
						}
						list.add(e);
					}
				}
			}
		};
		return list;
	}
	
	/** 迭代数据
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param targetKeyAfter 目标之后的数据
	 * @param limit
	 * @param eachResult
	 * @return
	 */
	protected <T extends ProtostrffSchema> List<T> forEach(DataSource dataSource, String familyName, 
			String targetKeyAfter, int limit, EachResult<T> eachResult) {
		List<T> list = new ArrayList<T>();
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				logger.debug("find family - {}, execute eachResult.", familyName);
				RocksIterator iterator = null;
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					Snapshot snapshot = db.getSnapshot();
					ReadOptions readOptions = dataSource.getReadOptions(snapshot);
					iterator = db.getIterator(readOptions);
					this.forEach(iterator, targetKeyAfter, list);
					db.clearSnapshot();
				} else {
					RocksDB db = dataSource.getRocksDB();
					iterator = db.newIterator(familyHandle);
					this.forEach(iterator, targetKeyAfter, list);
				}
			}
			
			private void forEach(RocksIterator iterator, String targetKeyAfter, List<T> list) {
				if (iterator != null) {
					if (targetKeyAfter != null && !targetKeyAfter.trim().equals("")) {
						iterator.seekForPrev(targetKeyAfter.getBytes());
					} else {
						iterator.seekToFirst();
					}
					int i = 0;
					for(; iterator.isValid(); iterator.next()) {
						T e = eachResult.valueOf(iterator.key(), iterator.value());
						if (e == null) {
							continue;
						}
						list.add(e);
						if (i > limit) {
							break;
						}
					}
				}
			}
		};
		
		return list;
	}
	
	/** 删除对象
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param key
	 */
	public void delete(DataSource dataSource, String familyName, String key) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					db.delete(familyHandle, key.getBytes());
				} else {
					RocksDB db = dataSource.getRocksDB();
					db.delete(familyHandle, key.getBytes());
				}
				logger.debug("delete family - {}, key = {}.", familyName, key);
			}
		};
	}
	
	/** 批量删除
	 * 
	 * @param dataSource
	 * @param familyName
	 * @param keys
	 */
	public void delete(DataSource dataSource, String familyName, Set<String> keys) {
		new RocksHandle(dataSource, familyName) {
			@Override
			public void handle(DataSource dataSource, ColumnFamilyHandle familyHandle) throws Exception {
				if (dataSource.isTransaction()) {
					Transaction db = dataSource.getTransaction();
					for(String key : keys) {
						db.delete(familyHandle, key.getBytes());
						logger.debug("delete family - {}, key = {}.", familyName, key);
					}
				} else {
					RocksDB db = dataSource.getRocksDB();
					for(String key : keys) {
						db.delete(familyHandle, key.getBytes());
						logger.debug("delete family - {}, key = {}.", familyName, key);
					}
				}
			}
		};
	}
	
	/** 获取对象的family名称
	 * 
	 * @param cls
	 * @return
	 */
	protected String findFamilyName(Class<?> cls) {
		if (cls.isAnnotationPresent(Family.class)) {
			Family family = cls.getAnnotation(Family.class);
			return family.name();
		}
		return null;
	}

}
