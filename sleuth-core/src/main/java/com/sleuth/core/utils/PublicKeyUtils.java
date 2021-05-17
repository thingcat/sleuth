package com.sleuth.core.utils;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

/** 公钥工具类
 * 
 * @author Jonse
 * @date 2018年11月8日
 */
public class PublicKeyUtils {
	
	/**
     * 双重Hash
     *
     * @param data
     * @return
     */
    public static byte[] doubleHash(byte[] data) {
        return DigestUtils.sha256(DigestUtils.sha256(data));
    }

    /**
     * 计算公钥的 RIPEMD160 Hash值
     *
     * @param pubKey 公钥
     * @return ipeMD160Hash(sha256 ( pubkey))
     */
    public static byte[] ripeMD160Hash(byte[] pubKey) {
        //1. 先对公钥做 sha256 处理
        byte[] shaHashedKey = DigestUtils.sha256(pubKey);
        RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
        ripemd160.update(shaHashedKey, 0, shaHashedKey.length);
        byte[] output = new byte[ripemd160.getDigestSize()];
        ripemd160.doFinal(output, 0);
        return output;
    }

    /**
     * 生成公钥的校验码
     *
     * @param payload
     * @return
     */
    public static byte[] checksum(byte[] payload) {
        return Arrays.copyOfRange(doubleHash(payload), 0, Base58Check.ADDRESS_CHECKSUM_LEN);
    }
	
    public static byte[] addressToPubKeyHash(String address) {
    	// 反向转化为 byte 数组
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        return Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
    }
    
}
