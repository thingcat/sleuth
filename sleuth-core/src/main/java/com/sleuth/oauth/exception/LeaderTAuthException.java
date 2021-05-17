package com.sleuth.oauth.exception;

/** 引入外部身份出错
 * 
 * @author Jonse
 * @date 2019年9月30日
 */
public class LeaderTAuthException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8219193782003889983L;
	
	private String message;
	
	public LeaderTAuthException(String message) {
		this.message = message;
	}
	
	public LeaderTAuthException(Throwable throwable) {
		this.message = throwable.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
