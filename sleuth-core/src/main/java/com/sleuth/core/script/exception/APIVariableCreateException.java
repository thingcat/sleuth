package com.sleuth.core.script.exception;

public class APIVariableCreateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2536805217194531226L;
	
	private String message;
	
	public APIVariableCreateException(String var) {
		this.message = var + " create error";
	}

	public String getMessage() {
		return message;
	}
	
}
