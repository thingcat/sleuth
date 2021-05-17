package com.sleuth.api;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/** json 返回对象
 * 
 * @author Jonse
 * @param <T>
 * @date 2019年3月13日
 */	
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonReponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7315985849769493168L;

	final static int DEFAULT_STATUS_CODE = 200;
	
	private boolean success;
	private Object data;
	private Integer code;
	private String message;
	
	public JsonReponse() {
		this.success = true;
		this.code = DEFAULT_STATUS_CODE;
	}
	
	public <T> JsonReponse(T value) {
		this.success = true;
		this.code = DEFAULT_STATUS_CODE;
		this.setData(value);
	}
	
	public <T> JsonReponse(List<T> list) {
		this.success = true;
		this.code = DEFAULT_STATUS_CODE;
		this.setData(list);
	}
	
	public void failed() {
		this.success = false;
		this.code = DEFAULT_STATUS_CODE;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

	public <T> void setData(T data) {
		if (data instanceof List) {
			this.data = data;
		} else {
			this.data = data;
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
