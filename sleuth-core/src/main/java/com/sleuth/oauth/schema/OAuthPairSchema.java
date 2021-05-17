package com.sleuth.oauth.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class OAuthPairSchema extends ProtostrffSchema implements Message<OAuthPair> {
	
	@Override
	public Schema<OAuthPair> cachedSchema() {
		return this.getSchema(OAuthPair.class);
	}
	
}
