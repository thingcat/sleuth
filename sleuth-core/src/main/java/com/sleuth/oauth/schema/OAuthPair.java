package com.sleuth.oauth.schema;

import java.io.Serializable;
import java.security.KeyPair;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import com.sleuth.core.storage.annotation.Family;
import com.sleuth.oauth.keypair.KeyPairUtils;

/** 证书身份
 * 
 * @author Administrator
 *
 */
@Family(name="oauthpair")
public class OAuthPair extends OAuthPairSchema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6401210514689011613L;
	
	/** 显性地址 */
	private String address;
	/** 私钥 */
	private byte[] privateKey;
	/** 公钥 */
	private byte[] publicKey;
	
	public OAuthPair() {
		
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public byte[] getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(byte[] privateKey) {
		this.privateKey = privateKey;
	}
	public byte[] getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}
	@Override
	public String toUnique() {
		return this.address;
	}
	
	/** 获取密钥对
	 * 
	 * @return
	 */
	public KeyPair getKeyPair() {
		try {
			BCECPublicKey publicKey = KeyPairUtils.loadPublicKey(this.getPublicKey());
			BCECPrivateKey privateKey = KeyPairUtils.loadPrivateKey(this.getPrivateKey());
			return new KeyPair(publicKey, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
