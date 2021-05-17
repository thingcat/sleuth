package com.sleuth.oauth.schema;

import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.MacUtils;

/** 认证密码
 * 
 * @author Jonse
 * @date 2019年5月14日
 */
@Family(name="crypto")
public class Crypto extends CryptoSchema {
	
	private String password;
	private String salt;
	
	public Crypto() {
		
	}
	
	public Crypto(String password, String salt) {
		this.password = password;
		this.salt = salt;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	@Override
	public String toUnique() {
		return MacUtils.getLocalMac();
	}

}
