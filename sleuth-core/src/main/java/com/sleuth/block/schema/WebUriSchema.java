package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public abstract class WebUriSchema extends ProtostrffSchema implements Message<WebUri> {

	@Override
	public Schema<WebUri> cachedSchema() {
		return this.getSchema(WebUri.class);
	}

}
