package com.sleuth.block.exception;

public class ThingSignatureExcption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5337982069284051329L;
	
	private String message;
	
	public ThingSignatureExcption() {
		this.message = "This action has no output.";
	}
	
	public ThingSignatureExcption(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
