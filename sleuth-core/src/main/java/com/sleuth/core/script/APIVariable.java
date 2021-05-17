package com.sleuth.core.script;

/** API变量
 * 
 * @author Jonse
 * @date 2020年12月8日
 */
public interface APIVariable {
	
	/** 获得Java内置对象
	 * 
	 * @param variable
	 * @return
	 */
	public abstract Object require(String variable);
	
	/** 设置Java内置对象
	 * 
	 * @param variable
	 * @param object
	 */
	public abstract void setVar(String variable, Class<?> clazz);
	
}
