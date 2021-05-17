package com.sleuth.core.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/** 获取本机的MAC地址
 * 
 * @author Jonse
 * @date 2020年9月16日
 */
public class MacUtils {
	
	/** 返回本机的MAC地址
	 * 
	 * @return
	 */
	public static String getLocalMac() {
		try {
			//得到IP，输出MS-20180329IPQG/192.168.1.2
			InetAddress inetAddress = InetAddress.getLocalHost();
			return getLocalMac(inetAddress);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getLocalMac(InetAddress inetAddress) throws SocketException {
		// 获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				buffer.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				buffer.append("0" + str);
			} else {
				buffer.append(str);
			}
		}
		return buffer.toString().toUpperCase();
	}

}
