package com.sleuth.core.config;

public class GenesisConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -520407940449390209L;
	
	private String message;
	
	public GenesisConfigException() {
		this.message = "An error occurred loading the genesis file";
	}
	
	public GenesisConfigException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
