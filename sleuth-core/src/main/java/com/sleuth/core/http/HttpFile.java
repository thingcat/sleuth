package com.sleuth.core.http;

public class HttpFile {
	
	private String name;
	private String filePath;
	private long size;
	private String suffix;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	@Override
	public String toString() {
		return "HttpFile [name=" + name + ", filePath=" + filePath + ", size=" + size + ", suffix=" + suffix + "]";
	}

}
