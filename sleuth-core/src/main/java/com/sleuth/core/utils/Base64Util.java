package com.sleuth.core.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	
	public static String toString(byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}
	
	public static byte[] toBinary(String base64String) {
		return Base64.decodeBase64(base64String);
	}

}
