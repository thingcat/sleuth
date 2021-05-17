package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class UTXOSchema extends ProtostrffSchema implements Message<UTXO> {
	
	@Override
	public Schema<UTXO> cachedSchema() {
		return this.getSchema(UTXO.class);
	}
	
}
