package com.sleuth.oauth.exception;

public class CreaterAuthKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 579559622032158229L;
	
	private String message;
	
	public CreaterAuthKeyException(String message) {
		this.message = message;
	}
	
	public CreaterAuthKeyException(Throwable throwable) {
		this.message = throwable.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
