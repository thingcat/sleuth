package com.sleuth.block.exception;

public class ActionNotDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6511067801021224749L;
	
	private String message;
	
	public ActionNotDataException() {
		this.message = "No action data was found.";
	}
	
	public ActionNotDataException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
