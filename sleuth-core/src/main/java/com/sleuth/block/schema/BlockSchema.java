package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class BlockSchema extends ProtostrffSchema implements Message<Block> {

	/** 缓存对象的schema
	 * 
	 */
	@Override
	public Schema<Block> cachedSchema() {
		return this.getSchema(Block.class);
	}
	
}
