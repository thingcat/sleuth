package com.sleuth.core.socket.message;

import java.io.Serializable;

/** 协议
 * 
 * @author Administrator
 *
 */
public class Protocol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3484791973353638438L;
	
	private String ch;//消息频道
	private int stage;//消息阶段
	private Long createAt;//消息推送时间
	
	public Protocol() {
		this.createAt = System.currentTimeMillis();
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	
}
