package com.sleuth.core.storage.exception;

/** 批处理异常
 * 
 * @author Jonse
 * @date 2020年8月24日
 */
public class BatchStoreException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4265154401608157000L;
	
	private String message;
	
	public BatchStoreException(Exception e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}
	
}
