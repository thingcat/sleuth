package com.sleuth.core.storage;

public interface Storage {
	
	/** 根据familyName获得一组数据
	 * 
	 * @param familyName
	 * @return
	 */
	public abstract <K, V> StoreResult<K, V> get(String familyName);
	
	/** 根据key获得数据
	 * 
	 * @param key
	 * @return
	 */
	public abstract <V> V get(String familyName, String key);
	
	
	
}
