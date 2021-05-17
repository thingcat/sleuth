package com.sleuth.api.exception;

/** 无效签名异常
 * 
 * @author Jonse
 * @date 2021年1月15日
 */
public class SignInvalidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3361751606958669706L;
	
	private String message;
	
	public SignInvalidException() {
		this.message = "This invalid signature.";
	}
	
	public SignInvalidException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
