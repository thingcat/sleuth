package com.sleuth.network.message.protocol;

import com.sleuth.core.socket.message.Protocol;

/** 主动申请同步消息
 * 
 * <p>订阅指令返回格式：{"event":"apply", "ch":"block/sync", "stage":0, "args":"json"}</p>
 * 
 * @author Jonse
 * @date 2020年10月16日
 */
public class ApProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7656645270999266402L;
	
	private String event;//消息类型
	private String args;//参数，用于扩展
	
	private ApProtocol() {
		
	}
	
	/** 创建一个处于第0阶段的消息订阅协议
	 * 
	 * @param stage
	 * @return
	 */
	public static ApProtocol newProtocol(CmdType ch) {
		ApProtocol protocol = new ApProtocol();
		protocol.setEvent(EvType.apply.getEvent());
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

	@Override
	public String toString() {
		return "ApProtocol [event=" + event + ", args=" + args + "]";
	}
}
