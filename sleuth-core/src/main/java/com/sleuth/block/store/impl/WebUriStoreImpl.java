package com.sleuth.block.store.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.WebUri;
import com.sleuth.block.store.WebUriStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class WebUriStoreImpl implements WebUriStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<WebUri> serializer = ProtostuffSerializer.of(new WebUri().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void add(WebUri webURI) {
		this.rocksTemplate.set(WebUri.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(webURI.getWxId().getBytes(), serializer.serialize(webURI));
			}
		});
	}

	@Override
	public WebUri get(String wxId) {
		return this.rocksTemplate.get(WebUri.class, wxId, new StoreValueSetter<WebUri>() {
			@Override
			public WebUri setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

	@Override
	public List<WebUri> getWebURIs(byte[] pubKeyHash) {
		
		return null;
	}

}
