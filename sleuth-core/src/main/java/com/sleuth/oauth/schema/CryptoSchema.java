package com.sleuth.oauth.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class CryptoSchema extends ProtostrffSchema implements Message<Crypto> {
	
	@Override
	public Schema<Crypto> cachedSchema() {
		return this.getSchema(Crypto.class);
	}
	
}
