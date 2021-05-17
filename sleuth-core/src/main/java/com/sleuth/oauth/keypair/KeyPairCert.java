package com.sleuth.oauth.keypair;

import java.security.KeyPair;

import com.sleuth.oauth.schema.OAuthPair;

public interface KeyPairCert {
	
	/** 创建身份
	 * 
	 * @return
	 */
	public abstract OAuthPair createAuth() throws Exception;
	
	/** 恢复密钥对
	 * 
	 * @param oAuthKey
	 * @return
	 */
	public abstract KeyPair recovery(OAuthPair oAuthKey) throws Exception;
	
	/** 恢复密钥对
	 * 
	 * @param privateBase64Key
	 * @param publicBase64Key
	 * @return
	 * @throws Exception
	 */
	public abstract KeyPair recovery(String privateBase64Key, String publicBase64Key) throws Exception;
	
}
