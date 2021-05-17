package com.sleuth.core.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Enums {

	/** 值类型
	 * 
	 * @return
	 */
	public String[] enums();
	
	/** 错误提示消息
	 * 
	 * @return
	 */
	public String message();
}
