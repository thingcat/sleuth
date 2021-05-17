package com.sleuth.oauth.service.impl;

import java.security.KeyPair;
import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.core.utils.Base58Check;
import com.sleuth.core.utils.MacUtils;
import com.sleuth.oauth.exception.CreaterAuthKeyException;
import com.sleuth.oauth.keypair.KeyPairCert;
import com.sleuth.oauth.keypair.KeyPairFactory;
import com.sleuth.oauth.keypair.KeyPairUtils;
import com.sleuth.oauth.schema.OAuthPair;
import com.sleuth.oauth.service.KeyPairService;
import com.sleuth.oauth.store.KeyPairStore;

@Service
public class KeyPairServiceImpl implements KeyPairService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final KeyPairCert keyPairCert = KeyPairFactory.getInstance(KeyPairFactory.ALGORITHM_ECDSA);
	
	@Resource
	private KeyPairStore keyPairStore;
	
	@Override
	public OAuthPair creater() {
		try {
			logger.info("Detect OAuthPair information at {}", MacUtils.getLocalMac());
			OAuthPair oAuthPair = keyPairStore.get();
			if (oAuthPair != null) {
				logger.warn("OAuthPair created, address = {}", oAuthPair.getAddress());
				return oAuthPair;
			}
			oAuthPair = keyPairCert.createAuth();
			keyPairStore.add(oAuthPair);
			logger.warn("OAuthPair address = {}", oAuthPair.getAddress());
			return oAuthPair;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CreaterAuthKeyException(e.getCause());
		}
	}
	
	@Override
	public OAuthPair creater(String privateBase64Key, String publicBase64Key) {
		try {
			BCECPrivateKey privateKey = KeyPairUtils.loadPrivateKey(privateBase64Key);
			BCECPublicKey publicKey = KeyPairUtils.loadPublicKey(publicBase64Key);
			byte[] encodedPublicKey = KeyPairUtils.encodedPublicKey(publicKey);
			String p2pkh = KeyPairUtils.createByP2PKH(publicKey);
			OAuthPair oAuthPair = new OAuthPair();
			oAuthPair.setPrivateKey(KeyPairUtils.encodedPrivateKey(privateKey));
			oAuthPair.setPublicKey(encodedPublicKey);
			oAuthPair.setAddress(p2pkh);
			keyPairStore.add(oAuthPair);
			logger.warn("OAuthPair address = {}", oAuthPair.getAddress());
			return oAuthPair;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CreaterAuthKeyException(e.getCause());
		}
	}

	@Override
	public KeyPair get() {
		OAuthPair oAuthPair = keyPairStore.get();
		if (oAuthPair != null) {
			try {
				return keyPairCert.recovery(oAuthPair);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CreaterAuthKeyException(e.getCause());
			}
		}
		return null;
	}

	@Override
	public OAuthPair getOAuthPair() {
		return keyPairStore.get();
	}

	@Override
	public String convertToHexString(String addr) {
		// 检查钱包地址是否合法
        Base58Check.base58ToBytes(addr);
		// 得到公钥Hash值
        byte[] versionedPayload = Base58Check.base58ToBytes(addr);
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
		return Hex.encodeHexString(pubKeyHash);
	}

}
