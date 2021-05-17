package com.sleuth.core.socket.message;

import java.io.Serializable;

/** 数据传输对象
 * 
 * @author Jonse
 * @date 2021年2月7日
 */
public abstract class DTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4856026849194370817L;
	
	private String uid;//消息在全网传播的唯一编号
	private String bc;//播报方-Broadcast

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

}
