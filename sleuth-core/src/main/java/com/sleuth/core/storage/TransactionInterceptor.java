package com.sleuth.core.storage;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.storage.annotation.Transactional;

/** 事务拦截器
 * 
 * @author Jonse
 * @date 2021年2月4日
 */
public class TransactionInterceptor implements MethodInterceptor {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private RocksTemplate rocksTemplate;

	@Override
	public Object invoke(MethodInvocation invoke) throws Throwable {
		Method method = invoke.getMethod();
		if (method.isAnnotationPresent(Transactional.class)) {
			//开启事务
			this.rocksTemplate.beginTransaction();
			logger.debug("Open Transaction...............");
			try {
				Object result = invoke.proceed();
				this.rocksTemplate.commit();
				logger.debug("Commit Transaction...............");
				return result;
			} catch (Exception e) {
				logger.error("db exception, rollback.");
				this.rocksTemplate.rollback();
				logger.error("Rollback Transaction...............");
				return null;
			}
		}
		return invoke.proceed();
	}
	
}
