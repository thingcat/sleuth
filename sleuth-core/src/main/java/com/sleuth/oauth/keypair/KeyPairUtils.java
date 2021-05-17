package com.sleuth.oauth.keypair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

import com.sleuth.core.utils.Base58Check;
import com.sleuth.core.utils.PublicKeyUtils;
import com.sleuth.oauth.exception.CreaterAuthKeyException;

/** 密钥对工具类
 * 
 * @author Jonse
 * @date 2021年3月7日
 */
public class KeyPairUtils {
	
	public static final String ALGORITHM_ECDSA = "ECDSA";
	public static final String SIGN_ALGORITHM = "SHA256withECDSA";
	public static final String EC_SPEC = "secp256k1";
	
	static {
		// 注册 BC Provider
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/** 创建密钥对
	 * 
	 * @return
	 * @throws Exception
	 */
	public static KeyPair newKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_ECDSA, BouncyCastleProvider.PROVIDER_NAME);
		// 椭圆曲线（EC）域参数设定
		// secp256k1，详见：https://bitcointalk.org/index.php?topic=151120.0
		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(EC_SPEC);
		keyPairGenerator.initialize(ecSpec, new SecureRandom());
		return keyPairGenerator.generateKeyPair();
	}
	
	/**
     * Get the uncompressed encoding of the public key point. The resulting array
     * should be 65 bytes length and start with 0x04 followed by the x and y
     * coordinates (32 bytes each).
     *
     * @param publicKey
     * @return
     */
    public static byte[] encodedPublicKey(BCECPublicKey publicKey) {
        return publicKey.getQ().getEncoded(false);
    }
    
    /**
     * Get the uncompressed encoding of the private key point. The resulting array
     * should be 65 bytes length and start with 0x04 followed by the x and y
     * coordinates (32 bytes each).
     *
     * @param privateKey
     * @return
     */
    public static byte[] encodedPrivateKey(BCECPrivateKey privateKey) {
        return privateKey.getD().toByteArray();
    }

    /**
     * Base64-decode a string. Works for both url-safe and non-url-safe
     * encodings.
     *
     * @param base64Encoded
     * @return
     */
    public static byte[] base64Decode(String base64Encoded) {
    	return Base64.decodeBase64(base64Encoded);
    }
    
    /**
     * Base64-decode a string. Works for both url-safe and non-url-safe
     * encodings.
     *
     * @param base64Encoded
     * @return
     */
    public static String base64Encoded(byte[] binaryDecoded) {
    	return Base64.encodeBase64String(binaryDecoded);
    }
    
    /**
     * Load the public key from a URL-safe base64 encoded string. Takes into
     * account the different encodings, including point compression.
     *
     * @param encodedPublicKey
     */
    public static BCECPublicKey loadPublicKey(String encodedPublicKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPublicKey = base64Decode(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA, BouncyCastleProvider.PROVIDER_NAME);
        ECParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_SPEC);
        ECCurve curve = parameterSpec.getCurve();
        ECPoint point = curve.decodePoint(decodedPublicKey);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, parameterSpec);
        return (BCECPublicKey) keyFactory.generatePublic(pubSpec);
    }
    
    /**
     * Load the public key from a URL-safe base64 encoded string. Takes into
     * account the different encodings, including point compression.
     *
     * @param encodedPublicKey
     */
    public static BCECPublicKey loadPublicKey(byte[] decodedPublicKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA, BouncyCastleProvider.PROVIDER_NAME);
        ECParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_SPEC);
        ECCurve curve = parameterSpec.getCurve();
        ECPoint point = curve.decodePoint(decodedPublicKey);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, parameterSpec);
        return (BCECPublicKey) keyFactory.generatePublic(pubSpec);
    }

    /**
     * Load the private key from a URL-safe base64 encoded string
     *
     * @param encodedPrivateKey
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static BCECPrivateKey loadPrivateKey(String encodedPrivateKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPrivateKey = base64Decode(encodedPrivateKey);
        BigInteger s = BigIntegers.fromUnsignedByteArray(decodedPrivateKey);
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_SPEC);
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, parameterSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA, BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
    
    /**
     * Load the private key from a URL-safe base64 encoded string
     *
     * @param encodedPrivateKey
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static BCECPrivateKey loadPrivateKey(byte[] decodedPrivateKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger s = BigIntegers.fromUnsignedByteArray(decodedPrivateKey);
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_SPEC);
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, parameterSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA, BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
    
    /** 使用私钥进行签名
     * 
     * @param privateKey
     * @param binaryDecoded
     * @return
     * @throws Exception
     */
    public static byte[] sign(BCECPrivateKey privateKey, byte[] data) throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }
    
    /** 使用公钥验证签名
     * 
     * @param publicKey 公钥hash
     * @param data 签名数据
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(BCECPublicKey publicKey, byte[] data, byte[] sign) throws Exception {
		Signature signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(sign);
	}
    
    public static final byte NETWORK_MAIN_ID = 0x00;
    public static final byte NETWORK_TEST_ID = 0x64;
    public static final byte NETWORK_NAMECOIN_ID = 0x34;
    
    /** 将公钥生成P2PKH格式地址
     * 
     * <p>P2PKH 是 Pay To PubKey Hash（付款至公钥哈希）的缩写，普通转账一般使用该地址</p>
     * 
     * @param publicKey
     * @return
     */
    public static String createByP2PKH(BCECPublicKey publicKey) {
    	try {
			return base58Check(NETWORK_MAIN_ID, publicKey);
		} catch (IOException e) {
			throw new CreaterAuthKeyException(e.getMessage());
		}
    }
    
    /** 将公钥转换为 base58Check，P2PKH格式
     * 
     * <p>P2PKH 是 Pay To PubKey Hash（付款至公钥哈希）的缩写，普通转账一般使用该地址</p>
     * 
     * @param nid
     * @param publicKey
     * @return
     * @throws IOException
     */
    private static String base58Check(byte nid, BCECPublicKey publicKey) throws IOException {
    	byte[] encodedPublicKey = encodedPublicKey(publicKey);
    	// 1. 获取 ripemdHashedKey
		byte[] ripemdHashedKey = PublicKeyUtils.ripeMD160Hash(encodedPublicKey);
		// 2. 添加版本 Main Network：0x00, Test Network：0x64，Namecoin Net：0x34
		ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
		addrStream.write(nid);
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
