package com.sleuth.block.store.impl;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.store.WxMerkleStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class WxMerkleStoreImpl implements WxMerkleStore {
	
	final Serializer<WxMerkle> serializer = ProtostuffSerializer.of(new WxMerkle().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(WxMerkle merkle) {
		rocksTemplate.set(TxMerkle.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(merkle.toUnique().getBytes(), serializer.serialize(merkle));
			}
		});
	}

	@Override
	public WxMerkle get(String merkleHash) {
		return rocksTemplate.get(WxMerkle.class, merkleHash, new StoreValueSetter<WxMerkle>() {
			@Override
			public WxMerkle setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

}
