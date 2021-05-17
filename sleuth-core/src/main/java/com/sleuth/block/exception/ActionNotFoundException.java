package com.sleuth.block.exception;

public class ActionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7889720333659616242L;

	private String message;
	
	public ActionNotFoundException() {
		this.message = "The action was not discovered.";
	}
	
	public ActionNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
