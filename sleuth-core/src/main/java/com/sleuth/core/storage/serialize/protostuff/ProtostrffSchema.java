package com.sleuth.core.storage.serialize.protostuff;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public abstract class ProtostrffSchema {

	static final Map<String, Schema<?>> cacheds = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	protected <T> Schema<T> getSchema(Class<T> cls) {
		Schema<T> schema = (Schema<T>) cacheds.get(cls);
		if (schema == null) {
			schema = RuntimeSchema.createFrom(cls);
			if (schema != null) {
				cacheds.put(cls.getName(), schema);
			}
		}
		return schema;
	}
	
	/** 设置唯一属性
	 * 
	 * @return
	 */
	public abstract String toUnique();
}
