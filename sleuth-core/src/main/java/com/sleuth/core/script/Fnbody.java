package com.sleuth.core.script;

/** 函数体
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class Fnbody {
	
	private String name;//函数名称
	private String version;//版本号
	private String body;//函数体
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
