package com.sleuth.core.storage.exception;

public class NotRocksTransactionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1340051705478469531L;
	
	private String message;
	
	public NotRocksTransactionException() {
		this.message = "not found RocksTransaction";
	}
	
	public NotRocksTransactionException(Exception e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}
	
}
