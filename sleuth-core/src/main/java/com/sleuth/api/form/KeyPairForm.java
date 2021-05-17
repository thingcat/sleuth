package com.sleuth.api.form;

import java.io.Serializable;

import com.sleuth.core.web.annotation.Length;
import com.sleuth.core.web.annotation.NotNull;

public class KeyPairForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7698845953738857517L;

	@NotNull(message="args.defect")
	@Length(min=64, max=512, message="form.oauth.privatekey.length")
	private String privateKey;
	
	@NotNull(message="args.defect")
	@Length(min=64, max=512, message="form.oauth.privatekey.length")
	private String publicKey;

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
