package com.sleuth.network.message.protocol;

import java.util.UUID;

import com.sleuth.core.socket.message.Protocol;

/** 数据推送消息
 * 
 * <p>订阅指令返回格式：{"event":"table", "ch":"block/sync", "data": "json" "args":"json"}</p>
 * 
 * <p>订阅指令返回格式：{"event":"table", "ch":"block/push", "args":"json"}</p>
 * 
 * @author Jonse
 * @date 2020年10月16日
 */
public class TabProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1817879698611165884L;
	
	private String uid;//消息ID
	private String bc;//播报方-Broadcast
	
	private String event;//消息类型
	private String data;//json格式数据
	private int used;//被消费过的次数
	private String args;//参数，用于扩展
	
	private TabProtocol() {
		
	}
	
	/** 创建一个处于第0阶段的消息订阅协议
	 * 
	 * @param stage
	 * @return
	 */
	public static TabProtocol newProtocol(CmdType ch) {
		TabProtocol protocol = new TabProtocol();
		protocol.setUid(UUID.randomUUID().toString());
		protocol.setEvent(EvType.table.getEvent());
		protocol.setCh(ch.getCh());
		protocol.setUsed(0);
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

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

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

	@Override
	public String toString() {
		return "TabProtocol [uid=" + uid + ", event=" + event + ", data=" + data + ", used=" + used + ", args=" + args
				+ "]";
	}
	
}
