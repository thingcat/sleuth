package com.sleuth.core.exception;

/** 同步关联异常
 * 
 * @author Jonse
 * @date 2020年11月29日
 */
public class SyncRelationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3132940249408833952L;
	
	static final String message = "Really associate data";

	public String getMessage() {
		return message;
	}
	
}
