package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

/** 默克尔树
 * 
 * @author Administrator
 *
 */
public abstract class WxMerkleSchema extends ProtostrffSchema implements Message<WxMerkle> {
	
	@Override
	public Schema<WxMerkle> cachedSchema() {
		return this.getSchema(WxMerkle.class);
	}
	
}
