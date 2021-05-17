package com.sleuth.oauth.store;

import com.sleuth.oauth.schema.OAuthPair;

public interface KeyPairStore {
	
	/** 保存
	 * 
	 * @param crypto
	 */
	public abstract void add(OAuthPair oAuthPair);
	
	/** 获得
	 * 
	 * @return
	 */
	public abstract OAuthPair get();

}
