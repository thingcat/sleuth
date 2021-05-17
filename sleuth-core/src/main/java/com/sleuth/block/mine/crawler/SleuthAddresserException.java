package com.sleuth.block.mine.crawler;

public class SleuthAddresserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7277608978144807132L;

	private String message;
	
	public SleuthAddresserException() {
		this.message = "failed to initialize the addresser.";
	}
	
	public SleuthAddresserException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
