package com.sleuth.core.storage.exception;

public class FamilyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6551272108312956061L;

	private String name;
	
	public FamilyExistsException(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return "Family ["+this.name+"] existed!";
	}
	
}
