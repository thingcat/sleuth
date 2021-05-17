package com.sleuth.oauth.service;

import java.security.KeyPair;

import com.sleuth.oauth.schema.OAuthPair;

public interface KeyPairService {
	
	/** 创建秘钥身份
	 * 
	 * @return
	 */
	public abstract OAuthPair creater();
	
	/** 创建秘钥身份
	 * 
	 * @param pirvateKey
	 * @param publicKey
	 * @return
	 */
	public abstract OAuthPair creater(String pirvateKey, String publicKey);
	
	/** 获得秘钥对
	 * 
	 * @return
	 */
	public abstract KeyPair get();
	
	/** 获得秘钥身份
	 * 
	 * @return
	 */
	public abstract OAuthPair getOAuthPair();
	
	/** 根据公钥地址转换为隐性地址
	 * 
	 * @param addr
	 * @return
	 */
	public abstract String convertToHexString(String addr);
}
