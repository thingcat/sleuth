package com.sleuth.block.store.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.Transaction;
import com.sleuth.block.store.TransactionBufferStore;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class TransactionBufferStoreImpl implements TransactionBufferStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<Transaction> serializer = ProtostuffSerializer.of(new Transaction().cachedSchema());
	
	static final String STORE_FAMILY_NAME = "transaction_buffer";
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void add(Transaction tx) {
		rocksTemplate.set(STORE_FAMILY_NAME, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(tx.getTxId().getBytes(), serializer.serialize(tx));
			}
		});
	}
	
	@Override
	public Transaction get(String txId) {
		return rocksTemplate.get(STORE_FAMILY_NAME, txId, new StoreValueSetter<Transaction>() {
			@Override
			public Transaction setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

	@Override
	public List<Transaction> list() {
		return rocksTemplate.forEach(STORE_FAMILY_NAME, new EachResult<Transaction>() {
			@Override
			public Transaction valueOf(byte[] key, byte[] value) {
				return serializer.deserialize(value);
			}
		});
	}

	@Override
	public void delete(String axHash) {
		rocksTemplate.delete(STORE_FAMILY_NAME, axHash);
	}

	@Override
	public void delete(String[] axHashs) {
		Set<String> keys = new HashSet<String>(Arrays.asList(axHashs));
		rocksTemplate.delete(STORE_FAMILY_NAME, keys);
	}

}
