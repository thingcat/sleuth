package com.sleuth.core.web.validator;

public class ValidatorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 592154068188373411L;
	
	// 错误代码
    private String code;
    private String field;
    private String message;
    
    public ValidatorException() {
    	
    }

    public ValidatorException(String code, String message) {
    	this.code = code;
        this.message = message;
    }
    
    public ValidatorException(String code, String field, String message) {
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

}
