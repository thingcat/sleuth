package com.sleuth.api.dto;

import java.io.Serializable;

/** 用户身份信息
 * 
 * @author Jonse
 * @date 2020年11月2日
 */
public class OAuthDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3584658206503693550L;

	/** 钱包地址 */
	private String address;
	/** 私钥 */
	private String privateKey;
	/** 公钥 */
	private String publicKey;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
}
