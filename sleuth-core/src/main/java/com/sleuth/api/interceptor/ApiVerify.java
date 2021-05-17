package com.sleuth.api.interceptor;

import javax.servlet.http.HttpServletRequest;

/** 签名验证
 * 
 * @author Jonse
 * @date 2021年1月14日
 */
public interface ApiVerify {
	
	/** 验证请求
	 * 
	 * @param request
	 * @return
	 */
	public abstract boolean verify(HttpServletRequest request);
	
}
