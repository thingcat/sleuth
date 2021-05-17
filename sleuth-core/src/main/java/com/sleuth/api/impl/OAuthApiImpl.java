package com.sleuth.api.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.api.OAuthApi;
import com.sleuth.api.dto.OAuthDTO;
import com.sleuth.api.form.KeyPairForm;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.oauth.schema.OAuthPair;
import com.sleuth.oauth.service.KeyPairService;

@Service
public class OAuthApiImpl implements OAuthApi {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private KeyPairService keyPairService;
	
	@Override
	public OAuthDTO get() {
		OAuthPair oAuthPair = keyPairService.getOAuthPair();
		if (oAuthPair != null) {
			OAuthDTO oAuth = new OAuthDTO();
			oAuth.setAddress(oAuthPair.getAddress());
			oAuth.setPrivateKey(Base64Util.toString(oAuthPair.getPrivateKey()));
			oAuth.setPublicKey(Base64Util.toString(oAuthPair.getPublicKey()));
			return oAuth;
		}
		return null;
	}

	@Override
	public OAuthDTO imports(KeyPairForm form) {
		OAuthPair oAuthPair = keyPairService.creater(form.getPrivateKey().trim(), form.getPublicKey().trim());
		if (oAuthPair != null) {
			OAuthDTO oAuth = new OAuthDTO();
			oAuth.setAddress(oAuthPair.getAddress());
			oAuth.setPrivateKey(Base64Util.toString(oAuthPair.getPrivateKey()));
			oAuth.setPublicKey(Base64Util.toString(oAuthPair.getPublicKey()));
			return oAuth;
		}
		return null;
	}

}
