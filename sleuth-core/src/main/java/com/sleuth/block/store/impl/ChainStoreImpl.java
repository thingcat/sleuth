package com.sleuth.block.store.impl;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.Chain;
import com.sleuth.block.store.ChainStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class ChainStoreImpl implements ChainStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<Chain> serializer = ProtostuffSerializer.of(new Chain().cachedSchema());
	
	static final String UNIQUE_KEY = "main_chain";
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public Chain get() {
		return rocksTemplate.get(Chain.class, UNIQUE_KEY, new StoreValueSetter<Chain>() {
			@Override
			public Chain setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

	@Override
	public void set(Long height, String blockHash) {
		Chain chain = new Chain(height, blockHash);
		rocksTemplate.set(Chain.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(UNIQUE_KEY.getBytes(), serializer.serialize(chain));
			}
		});
	}

}
