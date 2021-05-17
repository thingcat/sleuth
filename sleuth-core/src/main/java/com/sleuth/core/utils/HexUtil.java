package com.sleuth.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HexUtil {
	
	/** Calculates the SHA-256 digest and returns the value as a hex string.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		return DigestUtils.sha256Hex(bytes);
	}
	
	/** Calculates the SHA-256 digest and returns the value as a byte[]. 
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToBytes(String hex) {
		return DigestUtils.sha256(hex);
	}
	
	/** Calculates the SHA-256 digest and returns the value as a object.
	 * 
	 * @param object
	 * @return
	 */
	public static String objectToHex(Object object) {
		byte[] bytes = SerializeUtils.serialize(object);
		return bytesToHex(bytes);
	}
	
}
