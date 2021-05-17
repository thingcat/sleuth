package com.sleuth.block.exception;

public class SignActionErroException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473357804760169867L;

	private String message;
	
	public SignActionErroException() {
		this.message = "An error occurred in signing.";
	}
	
	public SignActionErroException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
