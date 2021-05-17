package com.sleuth.core.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpResult {
	
	private int statusCode;
	private String contentType;
	private String content;
	private String error;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public JSONObject getJSONObject() {
		if (this.content != null) {
			return JSON.parseObject(this.content);
		}
		return null;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	@Override
	public String toString() {
		return "HttpResult [statusCode=" + statusCode + ", contentType=" + contentType + ", content=" + content
				+ ", error=" + error + "]";
	}
	
}
