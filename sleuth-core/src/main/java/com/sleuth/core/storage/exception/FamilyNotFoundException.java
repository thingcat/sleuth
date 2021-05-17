package com.sleuth.core.storage.exception;

public class FamilyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6267848828914517866L;
	
	private String name;
	
	public FamilyNotFoundException() {
		
	}
	
	public FamilyNotFoundException(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return "Family ["+this.name+"] not found!";
	}
	
}
