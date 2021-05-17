package com.sleuth.api.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/** API请求拦截器
 * 
 * @author Jonse
 * @date 2021年1月15日
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private ApiVerify apiVerify;
	
	/** 请求到达Controller之前拦截
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return apiVerify.verify(request);
	}

}
