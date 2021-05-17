package com.sleuth.core.storage;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

/** 迭代结果
 * 
 * @author Jonse
 * @date 2020年1月19日
 * @param <T>
 */
public interface EachResult<T extends ProtostrffSchema> {
	
	public abstract T valueOf(byte[] key, byte[] value);
	
}
