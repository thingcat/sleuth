package com.sleuth.core.script.actuator;

import java.io.Serializable;

/** 输出结果
 * 
 * @author Jonse
 * @date 2020年12月12日
 */
public class OutputResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -838775121563490348L;
	
	private String axId;//事件ID
	private Object data;//输出结果，可能是一个复杂对象
	private String sign;//输出签名
	
	public String getAxId() {
		return axId;
	}
	public void setAxId(String axId) {
		this.axId = axId;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
