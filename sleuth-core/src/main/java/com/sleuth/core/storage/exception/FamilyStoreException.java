package com.sleuth.core.storage.exception;

public class FamilyStoreException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 449470221519107667L;
	
	private String message;
	
	public FamilyStoreException(String message) {
		this.message = "Error executing storage family. family = " + message;
	}
	
	public FamilyStoreException(Exception e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}
	
}
