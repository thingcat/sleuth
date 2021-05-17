package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class ChainSchema extends ProtostrffSchema implements Message<Chain> {

	/** 缓存对象的schema
	 * 
	 */
	@Override
	public Schema<Chain> cachedSchema() {
		return this.getSchema(Chain.class);
	}
	
}
