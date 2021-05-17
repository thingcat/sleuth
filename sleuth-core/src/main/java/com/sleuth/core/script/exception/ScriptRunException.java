package com.sleuth.core.script.exception;

public class ScriptRunException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -176484518473576456L;
	
	private String message;
	
	public ScriptRunException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
