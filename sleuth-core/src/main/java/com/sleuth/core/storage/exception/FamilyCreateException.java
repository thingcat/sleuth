package com.sleuth.core.storage.exception;

public class FamilyCreateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 449470221519107667L;
	
	private String message;
	
	public FamilyCreateException(String message) {
		this.message = "Error creating family. family = " + message;
	}
	
	public FamilyCreateException(Exception e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}
	
}
