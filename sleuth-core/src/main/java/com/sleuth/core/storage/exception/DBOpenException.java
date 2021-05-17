package com.sleuth.core.storage.exception;

public class DBOpenException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8638550382367354183L;
	
	private String message;
	
	public DBOpenException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
