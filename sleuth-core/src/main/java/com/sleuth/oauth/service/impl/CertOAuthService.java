package com.sleuth.oauth.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sleuth.core.utils.CryptoUtils;
import com.sleuth.core.utils.MD5Util;
import com.sleuth.oauth.exception.CreaterAuthKeyException;
import com.sleuth.oauth.exception.LeaderTAuthException;
import com.sleuth.oauth.keypair.KeyPairCert;
import com.sleuth.oauth.keypair.KeyPairFactory;
import com.sleuth.oauth.schema.OAuthPair;
import com.sleuth.oauth.schema.Crypto;
import com.sleuth.oauth.service.OAuthService;
import com.sleuth.oauth.store.CryptoStore;


/** 身份管理器
 * 
 * @author Administrator
 *
 */
@Service
public class CertOAuthService implements OAuthService {
	
	final static String DEFAULT_ALGORITHM = "AES";//身份证书加密算法
	final static String DEFAULT_AUTH_FILE = "auth.store";
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final KeyPairCert keyPairCert = KeyPairFactory.getInstance(KeyPairFactory.ALGORITHM_ECDSA);
	final OAuthCertLocal oAuthCertLocal;
	
	@Resource
	private CryptoStore cryptoStore;
	
	@Value("${auth.path}")
	private String authPath;
	
	public CertOAuthService() {
		oAuthCertLocal = new OAuthCertLocal();
	}
	
	@PostConstruct
	private void init() {
		File dir = new File(authPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	@Override
	public OAuthPair creater(String password) throws CreaterAuthKeyException {
		
		try {
			OAuthPair authKey = keyPairCert.createAuth();
			String cipherText = MD5Util.encrypt(password);
			//加盐加密算法保存密码，防止明文出现
			String salt = CryptoUtils.getSalt();  
		    String hashpwd = CryptoUtils.getHash(password, salt);
			Crypto crypto = new Crypto(hashpwd, salt);
			
			cryptoStore.add(crypto);
			oAuthCertLocal.saveToDisk(cipherText, authKey);
			
			return authKey;
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CreaterAuthKeyException(e.getCause());
		}
	}

	@Override
	public OAuthPair loader(String password) {
		Crypto crypto = cryptoStore.get();
		if (crypto != null) {
			String salt = crypto.getSalt();
			String hashpwd = crypto.getPassword();
			boolean result = CryptoUtils.verify(hashpwd, password, salt);
			if (result) {
				logger.debug("Read password successfully......");
				String cipherText = MD5Util.encrypt(password);
				return oAuthCertLocal.loaderFromDisk(cipherText);
			}
		}
		return null;
	}

	@Override
	public OAuthPair loader(String password, InputStream istream) {
		Crypto crypto = cryptoStore.get();
		if (crypto != null) {
			String salt = crypto.getSalt();
			String hashpwd = crypto.getPassword();
			boolean result = CryptoUtils.verify(hashpwd, password, salt);
			if (result) {
				logger.debug("Read password successfully......");
				String cipherText = MD5Util.encrypt(password);
				try {
					return oAuthCertLocal.loaderFramStream(cipherText, istream);
				} catch (LeaderTAuthException e) {
					logger.error("error: ", e);
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	@Override
	public KeyPair getKeyPair(OAuthPair certAuth) {
		try {
			return keyPairCert.recovery(certAuth);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/** 身份证书本地化操作
	 * 
	 * @author Jonse
	 * @date 2020年9月16日
	 */
	private class OAuthCertLocal {
		
		public OAuthCertLocal() {
			
		}
		
		/** 保存证书在硬盘
		 * 
		 * @param cipherText 密码内容
		 * @param certAuth
		 * @return
		 */
		public String saveToDisk(String cipherText, OAuthPair certAuth) {
			
			CipherOutputStream cos = null;
	    	ObjectOutputStream oos = null;
	    	FileOutputStream fos = null;
	    	BufferedOutputStream bos = null;
	    	
	    	//存储位置
	    	String storePath = authPath + "/" + DEFAULT_AUTH_FILE;
	        try                                                                                                                                                   {
	            SecretKeySpec sks = new SecretKeySpec(cipherText.getBytes(), DEFAULT_ALGORITHM);
	            // Create cipher
	            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, sks);
	            SealedObject sealedObject = new SealedObject(certAuth, cipher);
	            // Wrap the output stream
	            fos = new FileOutputStream(storePath);
	            bos = new BufferedOutputStream(fos);
	            cos = new CipherOutputStream(bos, cipher);
	            oos = new ObjectOutputStream(cos);
	            oos.writeObject(sealedObject);
	            logger.info("The identity certificate file is stored in: {}", storePath);
	        } catch (Exception e) {
	            logger.error("Failed to save identity certificate file for:{}", e.getMessage());
	            e.printStackTrace();
	            return null;
	        } finally {
	        	if (oos != null) {
					IOUtils.closeQuietly(oos);
				}
	        	if (cos != null) {
					IOUtils.closeQuietly(cos);
				}
	        	if (bos != null) {
	        		IOUtils.closeQuietly(bos);
				}
	        	if (fos != null) {
	        		IOUtils.closeQuietly(fos);
				}
			}
	        return storePath;
		}
		
		/** 外部加载
		 * 
		 * @param cipherText
		 * @param istream
		 * @return
		 * @throws LeaderTAuthException 
		 */
		public OAuthPair loaderFramStream(String cipherText, InputStream istream) throws LeaderTAuthException {
			BufferedInputStream bis = null;
	    	CipherInputStream cis = null;
	    	ObjectInputStream ois = null;
			try {
				SecretKeySpec sks = new SecretKeySpec(cipherText.getBytes(), DEFAULT_ALGORITHM);
				Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, sks);
				
				bis = new BufferedInputStream(istream);
	            cis = new CipherInputStream(bis, cipher);
	            ois = new ObjectInputStream(cis);
	            SealedObject sealedObject = (SealedObject) ois.readObject();
	            
	            return (OAuthPair) sealedObject.getObject(cipher);
	            
			} catch (Exception e) {
				logger.error("Error importing file, cause:{}", e.getMessage());
	            throw new LeaderTAuthException("Error introducing external identity certificate!");
			} finally {
				if (ois != null) {
					IOUtils.closeQuietly(ois);
				}
				if (cis != null) {
					IOUtils.closeQuietly(cis);
				}
				if (bis != null) {
					IOUtils.closeQuietly(bis);
				}
				if (istream != null) {
					IOUtils.closeQuietly(istream);
				}
			}
		}
		
		/** 从硬盘中读取身份
		 * 
		 * @param cipherText
		 * @return
		 */
		public OAuthPair loaderFromDisk(String cipherText) {
			FileInputStream fis = null;
	    	BufferedInputStream bis = null;
	    	CipherInputStream cis = null;
	    	ObjectInputStream ois = null;
	        try {
	            SecretKeySpec sks = new SecretKeySpec(cipherText.getBytes(), DEFAULT_ALGORITHM);
	            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
	            cipher.init(Cipher.DECRYPT_MODE, sks);
	            
	            fis = new FileInputStream(authPath + "/" + DEFAULT_AUTH_FILE);
	            bis = new BufferedInputStream(fis);
	            
	            cis = new CipherInputStream(bis, cipher);
	            ois = new ObjectInputStream(cis);
	            
	            SealedObject sealedObject = (SealedObject) ois.readObject();
	            return (OAuthPair) sealedObject.getObject(cipher);
	        } catch (Exception e) {
	            logger.error("Error importing file, cause:{}", e.getMessage());
	        } finally {
				if (ois != null) {
					IOUtils.closeQuietly(ois);
				}
				if (cis != null) {
					IOUtils.closeQuietly(cis);
				}
				if (bis != null) {
					IOUtils.closeQuietly(bis);
				}
				if (fis != null) {
					IOUtils.closeQuietly(fis);
				}
			}
	        throw new RuntimeException("Fail to load auth file from disk ! ");
		}
		
	}
	
}
