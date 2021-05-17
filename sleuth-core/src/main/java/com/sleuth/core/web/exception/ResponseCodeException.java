package com.sleuth.core.web.exception;

public class ResponseCodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6043178693337827395L;
	
	private String code;
	private String field;
	private String message;
	private Object object;
	
	public ResponseCodeException(String message) {
		this.message = message;
	}
	
	public ResponseCodeException(String message, Object object) {
		this.message = message;
		this.object = object;
	}
	
	public ResponseCodeException(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public ResponseCodeException(String code, String field, String message) {
		this.code = code;
		this.field = field;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
