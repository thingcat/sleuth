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
import com.sleuth.oauth.schema.OAuthPair;
import com.sleuth.oauth.store.KeyPairStore;

@Repository
public class KeyPairStoreImpl implements KeyPairStore  {
	
	final Serializer<OAuthPair> serializer = ProtostuffSerializer.of(new OAuthPair().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void add(OAuthPair oAuthPair) {
		rocksTemplate.set(OAuthPair.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				byte[] keyBytes = MacUtils.getLocalMac().getBytes();
				return new BytesObject(keyBytes, serializer.serialize(oAuthPair));
			}
		});
	}

	@Override
	public OAuthPair get() {
		String key = MacUtils.getLocalMac();
		return rocksTemplate.get(OAuthPair.class, key, new StoreValueSetter<OAuthPair>() {
			@Override
			public OAuthPair setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

}
