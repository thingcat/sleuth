package com.sleuth.block.store.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.Transaction;
import com.sleuth.block.store.TransactionStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BatchStoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class TransactionStoreImpl implements TransactionStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<Transaction> serializer = ProtostuffSerializer.of(new Transaction().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(Transaction tx) {
		rocksTemplate.set(Transaction.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(tx.getTxId().getBytes(), serializer.serialize(tx));
			}
		});
	}
	
	@Override
	public void add(Transaction[] transactions) {
		rocksTemplate.set(Transaction.class, new BatchStoreParameterSetter() {
			@Override
			public List<BytesObject> setValues(ColumnFamilyHandle familyHandle) throws Exception {
				List<BytesObject> params = new ArrayList<BytesObject>(transactions.length);
				for(Transaction tx : transactions) {
					BytesObject e = new BytesObject(tx.getTxId().getBytes(), serializer.serialize(tx));
					params.add(e);
				}
				return params;
			}
		});
	}
	
	@Override
	public Transaction get(String txId) {
		return rocksTemplate.get(Transaction.class, txId, new StoreValueSetter<Transaction>() {
			@Override
			public Transaction setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

}
