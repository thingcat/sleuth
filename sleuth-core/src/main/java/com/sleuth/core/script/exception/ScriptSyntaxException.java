package com.sleuth.core.script.exception;

public class ScriptSyntaxException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5396748963459827826L;
	
	private String message;
	
	public ScriptSyntaxException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
