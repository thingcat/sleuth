package com.sleuth.network.message.protocol;

import com.sleuth.core.socket.message.Protocol;

/** 订阅消息
 * 
 * <p>订阅指令返回格式：{"event":"subscribe", "ch":"block/sync", "stage":0, "args":"json"}</p>
 * 
 * @author Jonse
 * @date 2020年10月16日
 */
public class SubProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4025502987683725193L;
	
	private String event;//消息类型
	private String args;//参数，用于扩展
	
	private SubProtocol() {
		
	}
	
	/** 创建一个处于第0阶段的消息订阅协议
	 * 
	 * @param stage
	 * @return
	 */
	public static SubProtocol newProtocol(CmdType ch) {
		SubProtocol protocol = new SubProtocol();
		protocol.setEvent(EvType.subscribe.getEvent());
		protocol.setCh(ch.getCh());
		protocol.setStage(0);
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
