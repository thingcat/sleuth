package com.sleuth.block.exception;

public class ActionNotOutputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6063692064368723684L;
	
	private String message;
	
	public ActionNotOutputException() {
		this.message = "This action has no output.";
	}
	
	public ActionNotOutputException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
