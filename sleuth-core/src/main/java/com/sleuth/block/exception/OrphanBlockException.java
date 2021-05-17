package com.sleuth.block.exception;

/** 孤块异常
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public class OrphanBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6719609243142145430L;
	
	private String message;
	
	public OrphanBlockException() {
		this.message = "orphan block.";
	}
	
	public OrphanBlockException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
