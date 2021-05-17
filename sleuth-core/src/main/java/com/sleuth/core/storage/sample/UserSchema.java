package com.sleuth.core.storage.sample;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

public class UserSchema extends ProtostrffSchema implements Message<User> {

	@Override
	public Schema<User> cachedSchema() {
		return getSchema(User.class);
	}

	@Override
	public String toUnique() {
		// TODO Auto-generated method stub
		return null;
	}

}
