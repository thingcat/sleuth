package com.sleuth.oauth.service;

import java.io.InputStream;
import java.security.KeyPair;

import com.sleuth.oauth.exception.CreaterAuthKeyException;
import com.sleuth.oauth.schema.OAuthPair;

/** 身份管理
 * 
 * @author Administrator
 *
 */
public interface OAuthService {
	
	/** 创建身份
	 * 
	 * @param password
	 * @return
	 * @throws AuthExistException
	 */
	public abstract OAuthPair creater(String password) throws CreaterAuthKeyException;
	
	/** 从本地加载身份
	 * 
	 * @param password
	 * @return
	 */
	public abstract OAuthPair loader(String password);
	
	/** 获取密钥  公钥
	 * 
	 * @param certAuth
	 * @return
	 */
	public abstract KeyPair getKeyPair(OAuthPair certAuth);
	
	/** 从外部加载身份
	 * 
	 * @param password
	 * @param inputStream
	 * @return
	 */
	public abstract OAuthPair loader(String password, InputStream inputStream);

}
