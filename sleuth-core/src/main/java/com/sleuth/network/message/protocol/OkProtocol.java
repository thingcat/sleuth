package com.sleuth.network.message.protocol;

import com.sleuth.core.socket.message.Protocol;

/** 完成推送的协议
 * 
 * <p>订阅指令返回格式：{"event":"ok", "ch":"block/sync"}</p>
 * 
 * @author Jonse
 * @date 2021年1月21日
 */
public class OkProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1953671925914657944L;

	private String event;
	private String args;//参数，用于扩展
	
	private OkProtocol() {
		
	}
	
	public static OkProtocol newProtocol(CmdType ch) {
		OkProtocol protocol = new OkProtocol();
		protocol.setEvent(EvType.ok.getEvent());
		protocol.setCh(ch.getCh());
		return protocol;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}
	
}
