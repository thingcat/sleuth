package com.sleuth.core.config;

public class DefaultConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7923795831701822835L;
	
	private String message;
	
	public DefaultConfigException() {
		this.message = "An error occurred loading the configuration file";
	}
	
	public DefaultConfigException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
