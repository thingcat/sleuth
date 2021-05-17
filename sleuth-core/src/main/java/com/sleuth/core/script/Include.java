package com.sleuth.core.script;

/** 引用其他的函数
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class Include {

	private String name;//函数名
	private String alias;//别名
	
	public Include() {
		
	}
	
	public Include(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
