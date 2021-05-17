package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

/** RmATOR
 * 
 * @author Administrator
 *
 */
public abstract class RmUTXOSchema extends ProtostrffSchema implements Message<RmUTXO> {
	
	@Override
	public Schema<RmUTXO> cachedSchema() {
		return this.getSchema(RmUTXO.class);
	}
	
}
