package com.sleuth.block.store.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.WebUri;
import com.sleuth.block.store.WebUriBufferStore;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class WebUriBufferStoreImpl implements WebUriBufferStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<WebUri> serializer = ProtostuffSerializer.of(new WebUri().cachedSchema());
	
	static final String STORE_FAMILY_NAME = "web_uri_buffer";
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(WebUri webURI) {
		this.rocksTemplate.set(STORE_FAMILY_NAME, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(webURI.getWxId().getBytes(), serializer.serialize(webURI));
			}
		});
	}

	@Override
	public WebUri get(String wxId) {
		return this.rocksTemplate.get(STORE_FAMILY_NAME, wxId, new StoreValueSetter<WebUri>() {
			@Override
			public WebUri setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

	@Override
	public void remove(String wxId) {
		this.rocksTemplate.delete(STORE_FAMILY_NAME, wxId);
	}

	@Override
	public void remove(Set<String> wxIds) {
		this.rocksTemplate.delete(STORE_FAMILY_NAME, wxIds);
	}
	
	@Override
	public List<WebUri> list() {
		return this.rocksTemplate.forEach(STORE_FAMILY_NAME, new EachResult<WebUri>() {
			@Override
			public WebUri valueOf(byte[] key, byte[] value) {
				return serializer.deserialize(value);
			}
		});
	}
}
