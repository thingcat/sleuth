package com.sleuth.oauth.keypair;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.oauth.keypair.impl.ECKeyPairCert;

public class KeyPairFactory {
	
	static final Logger logger = LoggerFactory.getLogger(KeyPairFactory.class);
	
	public static final String ALGORITHM_EC = "EC";//ECDSA
	public static final String ALGORITHM_ECDSA = "ECDSA";
	
	public static final String SIGN_ALGORITHM = "SHA256withECDSA";
	
	public static final String EC_SPEC = "secp256k1";
	
	
	static final Map<String, KeyPairCert> cache = new HashMap<String, KeyPairCert>();
	
	static {
		cache.put(ALGORITHM_ECDSA, new ECKeyPairCert());
	}
	
	public static KeyPairCert getInstance(String algorithm) {
		return cache.get(algorithm);
	}
	
}
