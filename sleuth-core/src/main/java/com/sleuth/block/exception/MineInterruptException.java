package com.sleuth.block.exception;

/** 中断挖矿异常
 * 
 * @author Jonse
 * @date 2021年2月1日
 */
public class MineInterruptException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1720345725965684483L;
	
	private String message;
	
	public MineInterruptException() {
		this.message = "interruption of mining.";
	}
	
	public MineInterruptException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
