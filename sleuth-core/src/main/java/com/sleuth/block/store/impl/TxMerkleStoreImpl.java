package com.sleuth.block.store.impl;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.store.TxMerkleStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class TxMerkleStoreImpl implements TxMerkleStore {
	
	final Serializer<TxMerkle> serializer = ProtostuffSerializer.of(new TxMerkle().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(TxMerkle merkle) {
		rocksTemplate.set(TxMerkle.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(merkle.toUnique().getBytes(), serializer.serialize(merkle));
			}
		});
	}

	@Override
	public TxMerkle get(String merkleHash) {
		return rocksTemplate.get(TxMerkle.class, merkleHash, new StoreValueSetter<TxMerkle>() {
			@Override
			public TxMerkle setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

}
