package com.sleuth.oauth.store.impl;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.springframework.stereotype.Repository;

import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;
import com.sleuth.core.utils.MacUtils;
import com.sleuth.oauth.schema.Crypto;
import com.sleuth.oauth.store.CryptoStore;

@Repository
public class CryptoStoreImpl implements CryptoStore {
	
	final Serializer<Crypto> serializer = ProtostuffSerializer.of(new Crypto().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(Crypto crypto) {
		rocksTemplate.set(Crypto.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(crypto.toUnique().getBytes(), serializer.serialize(crypto));
			}
		});
	}

	@Override
	public Crypto get() {
		String key = MacUtils.getLocalMac();
		return rocksTemplate.get(Crypto.class, key, new StoreValueSetter<Crypto>() {
			@Override
			public Crypto setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

}
