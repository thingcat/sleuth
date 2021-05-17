package com.sleuth.network.message.protocol;

import com.sleuth.core.socket.message.Protocol;

/** 取消订阅消息
 * 
 * <p>订阅指令返回格式：{"event":"unsubscribe", "ch":"block/sync", "stage":0, "args":"json"}</p>
 * 
 * @author Jonse
 * @date 2020年10月16日
 */
public class UnsubProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6951353713612990406L;
	
	private String event;//消息类型
	private String args;//参数，用于扩展
	
	private UnsubProtocol() {
		
	}
	
	/** 创建一个处于第0阶段的消息取消订阅协议
	 * 
	 * @param stage
	 * @return
	 */
	public static UnsubProtocol newProtocol(CmdType ch) {
		UnsubProtocol protocol = new UnsubProtocol();
		protocol.setEvent(EvType.unsubscribe.getEvent());
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
