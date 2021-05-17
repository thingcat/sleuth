package com.sleuth.api.exception;

/** 请求超时异常
 * 
 * @author Jonse
 * @date 2021年1月15日
 */
public class TimeOutException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 904149468863243460L;

	private String message;
	
	public TimeOutException() {
		this.message = "This request timed out.";
	}
	
	public TimeOutException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
