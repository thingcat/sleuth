package com.sleuth.oauth.exception;

public class AuthExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367550771232982497L;

	private String message;
	
	public AuthExistException(String message) {
		this.message = message;
	}
	
	public AuthExistException(Throwable throwable) {
		this.message = throwable.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
