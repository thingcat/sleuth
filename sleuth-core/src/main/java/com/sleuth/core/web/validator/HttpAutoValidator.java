package com.sleuth.core.web.validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.web.annotation.Enums;
import com.sleuth.core.web.annotation.Escape;
import com.sleuth.core.web.annotation.Length;
import com.sleuth.core.web.annotation.NotNull;
import com.sleuth.core.web.annotation.Regex;

/** 请求参数校验
 * 
 * @date 2017/11/17
 * @author zhangg
 *
 */
public class HttpAutoValidator implements AutoVaildator {
	
	final Logger logger = LoggerFactory.getLogger(HttpAutoValidator.class);
	
	private HttpServletRequest httpRequest;
	private Field[] fields = null;
	private Field[] superFields = null;//父类字段
	
	public HttpAutoValidator(HttpServletRequest httpRequest, Class<?> formClass) {
		this.httpRequest = httpRequest;
		this.fields = formClass.getDeclaredFields();
		Class<?> superClass = formClass.getSuperclass();
		if (superClass != null) {
			superFields = superClass.getDeclaredFields();
		}
	}

	@Override
	public boolean validator() {
		
		//校验当前类
		for (Field field : fields) {
			vaildFields(field);
		}
		
		//校验父类
		if (superFields != null && superFields.length > 0) {
			for (Field field : superFields) {
				vaildFields(field);
			}
		}

		return true;
	}
	

	private boolean vaildFields(Field field) {
		String fieldName = field.getName();
		
		if (field.isAnnotationPresent(NotNull.class)) {
			NotNull empty = field.getAnnotation(NotNull.class);
			this.validNotNull(fieldName, empty);
		}
		
		if (field.isAnnotationPresent(Regex.class)) {
			Regex regex = field.getAnnotation(Regex.class);
			this.validRegex(fieldName, regex);
		}
		
		if (field.isAnnotationPresent(Length.class)) {
			Length length = field.getAnnotation(Length.class);
			this.validLength(fieldName, length);
		}
		
		if (field.isAnnotationPresent(Enums.class)) {
			Enums enums = field.getAnnotation(Enums.class);
			this.validEnums(fieldName, enums);
		}
		return true;
	}
	
	public boolean validNotNull(String fieldName, NotNull empty) {
		String value = httpRequest.getParameter(fieldName);
		if (value == null || value.length() < 1) {
			//如果为空，则看看默认值
			String defValue = empty.defValue();
			if (defValue == null || defValue.trim().length() < 1) {
				if (logger.isWarnEnabled()) {
					logger.warn("Validator field [{}] ----> Can not be null", fieldName);
				}
				throw new ValidatorException("valid.not.empty", fieldName, empty.message());
			}
		}
		return true;
	}
	
	public boolean validRegex(String fieldName, Regex regex) {
		String value = httpRequest.getParameter(fieldName);
		if (value != null && value.trim().length() > 0) {
			boolean matched = Pattern.matches(regex.regex(), value.toString().trim());
			if (!matched) {
				if (logger.isWarnEnabled()) {
					logger.warn("Validator field [{}={}] ----> Illegal input characters", fieldName, value);
				}
				throw new ValidatorException("valid.regex.illegal", fieldName, regex.message());
			}
		}
		return true;
	}
	
	public boolean validLength(String fieldName, Length length) {
		String value = httpRequest.getParameter(fieldName);
		if (value != null && value.length() > 0) {
			if (length.min() > 0) {
				if (value.length() < length.min()) {
					if (logger.isDebugEnabled()) {
						logger.warn("Validator field [{}={}] ----> to shot", fieldName, value);
					}
					throw new ValidatorException("valid.length.illegal", fieldName, length.message());
				}
			}
			
			if (value.length() > length.max()) {
				if (logger.isDebugEnabled()) {
					logger.warn("Validator field [{}={}] ----> to long", fieldName, value);
				}
				throw new ValidatorException("valid.length.illegal", fieldName, length.message());
			}
		}
		return true;
	}
	
	public boolean validEnums(String fieldName, Enums enums) {
		String value = httpRequest.getParameter(fieldName);
		List<String> array = Arrays.asList(enums.enums());
		if (!array.contains(value)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Validator field [{}={}] Illegal field, must be {}", 
						fieldName, value, enums.toString());
			}
			throw new ValidatorException("valid.enums.illegal", fieldName, enums.message());
		}
		return true;
	}
	
	public boolean validEscape(String fieldName, Escape escape) {
		
		return true;
	}

}
