package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

/** 默克尔树
 * 
 * @author Administrator
 *
 */
public abstract class TxMerkleSchema extends ProtostrffSchema implements Message<TxMerkle> {
	
	@Override
	public Schema<TxMerkle> cachedSchema() {
		return this.getSchema(TxMerkle.class);
	}
	
}
