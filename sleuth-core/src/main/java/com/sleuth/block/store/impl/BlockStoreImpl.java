package com.sleuth.block.store.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.Block;
import com.sleuth.block.store.BlockStore;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class BlockStoreImpl implements BlockStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<Block> serializer = ProtostuffSerializer.of(new Block().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public void add(Block newBlock) {
		this.rocksTemplate.set(Block.class, new StoreParameterSetter() {
			@Override
			public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
				return new BytesObject(newBlock.getHash().getBytes(), serializer.serialize(newBlock));
			}
		});
	}

	@Override
	public Block get(String hash) {
		return this.rocksTemplate.get(Block.class, hash, new StoreValueSetter<Block>() {
			@Override
			public Block setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

	@Override
	public List<Block> getBlocks(String hash, int limit) {
		List<Block> blocks = new ArrayList<Block>();
		this.forList(blocks, hash, limit);
		return blocks;
	}
	
	private void forList(List<Block> blocks, String hash, int limit) {
		Block block = this.get(hash);
		if (block != null) {
			blocks.add(block);
			String prevHash = block.getPrevBlockHash();
			if (blocks.size() == limit) {
				return;
			}
			this.forList(blocks, prevHash, limit);
		}
	}
}
