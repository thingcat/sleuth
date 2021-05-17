package com.sleuth.core.storage.datastore.setter;

/** 二进制对象参数
 * 
 * @author Jonse
 * @date 2020年8月26日
 */
public class BytesObject {
	
	private byte[] key;
	private byte[] value;
	
	public BytesObject() {
		
	}
	
	public BytesObject(byte[] key, byte[] value) {
		this.key = key;
		this.value = value;
	}
	
	public byte[] getKey() {
		return key;
	}
	public void setKey(byte[] key) {
		this.key = key;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	
}
