package com.sleuth.core.utils;

import java.security.MessageDigest;

import com.alibaba.fastjson.JSON;

/** 生成数字签名
 * 选择SHA256
 * @author Jonse
 * @date 2018年10月31日
 */
public class SHA256Utils {

	// Applies Sha256 to a string and returns the result.
	public static String applySha256(String input) {

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Applies sha256 to our input,
			byte[] hash = digest.digest(input.getBytes("UTF-8"));

			StringBuffer hexString = new StringBuffer(); // This will contain
															// hash as
															// hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Short hand helper to turn Object into a json string
	public static String getJson(Object o) {
		return JSON.toJSONString(o);
	}

	// Returns difficulty string target, to compare to hash. eg difficulty of 5
	// will return "00000"
	public static String getDificultyString(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

}
