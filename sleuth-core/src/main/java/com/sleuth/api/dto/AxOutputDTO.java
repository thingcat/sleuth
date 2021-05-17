package com.sleuth.api.dto;

import java.io.Serializable;

import com.sleuth.core.utils.Base64Util;

public class AxOutputDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3645125154135017814L;
	
	private Object data;//事件结果的数据输出
	private String pubKeyHash;//公钥Hash，通过计算公钥的 RIPEMD160 Hash值获得
	
	public AxOutputDTO() {
		
	}
	
	public AxOutputDTO(Object data, byte[] pubKeyHash) {
		this.data = data;
		this.pubKeyHash = Base64Util.toString(pubKeyHash);
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getPubKeyHash() {
		return pubKeyHash;
	}
	public void setPubKeyHash(String pubKeyHash) {
		this.pubKeyHash = pubKeyHash;
	}

}
