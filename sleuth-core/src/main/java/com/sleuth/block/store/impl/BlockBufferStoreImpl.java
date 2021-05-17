package com.sleuth.block.store.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.Block;
import com.sleuth.block.store.BlockBufferStore;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class BlockBufferStoreImpl implements BlockBufferStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<Block> serializer = ProtostuffSerializer.of(new Block().cachedSchema());
	
	static final String STORE_FAMILY_NAME = "block_buffer";
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void add(Block block) {
		this.rocksTemplate.set(STORE_FAMILY_NAME, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(block.getHash().getBytes(), serializer.serialize(block));
			}
		});
	}

	@Override
	public Block get(String hash) {
		return rocksTemplate.get(STORE_FAMILY_NAME, hash, new StoreValueSetter<Block>() {
			@Override
			public Block setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

	@Override
	public void remove(String hash) {
		rocksTemplate.delete(STORE_FAMILY_NAME, hash);
	}

	@Override
	public List<Block> list() {
		return rocksTemplate.forEach(STORE_FAMILY_NAME, new EachResult<Block>() {
			@Override
			public Block valueOf(byte[] key, byte[] value) {
				return serializer.deserialize(value);
			}
		});
	}

}
