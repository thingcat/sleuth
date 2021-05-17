package com.sleuth.oauth.keypair.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import com.sleuth.core.utils.Base58Check;
import com.sleuth.core.utils.PublicKeyUtils;
import com.sleuth.oauth.keypair.KeyPairCert;
import com.sleuth.oauth.keypair.KeyPairUtils;
import com.sleuth.oauth.schema.OAuthPair;

public class ECKeyPairCert implements KeyPairCert {
	
	@Override
	public OAuthPair createAuth() throws Exception {
		KeyPair keyPair = KeyPairUtils.newKeyPair();
		//返回密钥的PKCS8表示形式。
		BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
		BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
		
		byte[] privateKeyBytes = KeyPairUtils.encodedPrivateKey(privateKey);
		byte[] publicKeyBytes = KeyPairUtils.encodedPublicKey(publicKey);
		String addr = KeyPairUtils.createByP2PKH(publicKey);
		
		OAuthPair certAuth = new OAuthPair();
		certAuth.setAddress(addr);
		certAuth.setPrivateKey(privateKeyBytes);
		certAuth.setPublicKey(publicKeyBytes);
		return certAuth;
	}
	
	@Override
	public KeyPair recovery(OAuthPair certAuth) throws Exception {
		byte[] decodedPrivateKey = certAuth.getPrivateKey();
		byte[] decodedPublicKey = certAuth.getPublicKey();
		return new KeyPair(KeyPairUtils.loadPublicKey(decodedPublicKey), KeyPairUtils.loadPrivateKey(decodedPrivateKey));
	}
	
	@Override
	public KeyPair recovery(String encodedPrivateKey, String decodedPublicKey) throws Exception {
		return new KeyPair(KeyPairUtils.loadPublicKey(decodedPublicKey), KeyPairUtils.loadPrivateKey(encodedPrivateKey));
	}
	
	
	public static void main(String[] args) throws Exception {
		//1、向指定地址转账
		//2、向特定脚本转账
		// 创建椭圆曲线算法的密钥对生成器，算法为 ECDSA
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA");
        // 椭圆曲线（EC）域参数设定
		// bitcoin 为什么会选择
		// secp256k1，详见：https://bitcointalk.org/index.php?topic=151120.0
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        generator.initialize(ecGenParameterSpec, new SecureRandom());
        generator.initialize(256);
        KeyPair keyPair = generator.generateKeyPair();
        
		byte[] pubKey = keyPair.getPublic().getEncoded();
		
        System.out.println(Hex.encodeHexString(pubKey));
        String address = "18wVquzAA82mmAr7e1CQfmjEK52Y3gPjgP";//toBtcAddress(pubKey);
        System.out.println(address);
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        String hexHash = Hex.encodeHexString(pubKeyHash);
        System.out.println(hexHash);
	}
	
	static String toBtcAddress(byte[] publicKey) throws IOException {
		// 1. 获取 ripemdHashedKey
		byte[] ripemdHashedKey = PublicKeyUtils.ripeMD160Hash(publicKey);
		// 2. 添加版本 0x00
		ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
		addrStream.write((byte) 0);
		addrStream.write(ripemdHashedKey);
		byte[] versionedPayload = addrStream.toByteArray();
		// 3. 计算校验码
		byte[] checksum = PublicKeyUtils.checksum(versionedPayload);
		// 4. 得到 version + paylod + checksum 的组合
		addrStream.write(checksum);
		byte[] binaryAddress = addrStream.toByteArray();
		// 5. 执行Base58转换处理
		return Base58Check.rawBytesToBase58(binaryAddress);
	}
	
}
