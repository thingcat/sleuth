package com.sleuth.core.script.variable;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sleuth.core.script.APIVariable;
import com.sleuth.core.script.exception.APIVariableCreateException;
import com.sleuth.core.script.exception.APIVariableNotFoundException;

/** 全局变量
 * 
 * @author Jonse
 * @date 2020年12月8日
 */
public class GlobalAPIVariable implements APIVariable {
	
	static final Map<String, Class<?>> variables = Maps.newHashMap();
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object require(String variable) {
		Class<?> clazz = variables.get(variable);
		if (clazz == null) {
			throw new APIVariableNotFoundException(variable);
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("API create error, class={}", clazz.getName(), e);
			throw new APIVariableCreateException(variable);
		}
	}

	@Override
	public void setVar(String variable, Class<?> clazz) {
		variables.put(variable, clazz);
	}

}
