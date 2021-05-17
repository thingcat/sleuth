package com.sleuth.block.exception;

/** 重复区块异常
 * 
 * @author Jonse
 * @date 2021年2月6日
 */
public class ExistingBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1546957584238473906L;
	
	private String message;
	
	public ExistingBlockException() {
		this.message = "existing blocks.";
	}
	
	public ExistingBlockException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
