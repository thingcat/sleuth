package com.sleuth.oauth.store;

import com.sleuth.oauth.schema.Crypto;

public interface CryptoStore {
	
	/** 保存本地账户密码
	 * 
	 * @param crypto
	 */
	public abstract void add(Crypto crypto);
	
	/** 获得本地账户密码
	 * 
	 * @return
	 */
	public abstract Crypto get();

}
