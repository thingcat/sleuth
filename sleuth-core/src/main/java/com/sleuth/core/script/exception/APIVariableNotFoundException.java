package com.sleuth.core.script.exception;

public class APIVariableNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2536805217194531226L;
	
	private String message;
	
	public APIVariableNotFoundException(String var) {
		this.message = var + " not fund";
	}

	public String getMessage() {
		return message;
	}
	
}
