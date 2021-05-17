package com.sleuth.core.config;

public class CoinbaseConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4382338047962148610L;

	private String message;
	
	public CoinbaseConfigException() {
		this.message = "Error Coinbase configuration";
	}
	
	public CoinbaseConfigException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
