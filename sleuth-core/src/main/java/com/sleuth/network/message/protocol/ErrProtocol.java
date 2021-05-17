package com.sleuth.network.message.protocol;

import com.sleuth.core.socket.message.Protocol;

/** 错误的消息
 * 
 * <p>错误返回格式：{"event":"error", "ch":"block/sync", "errorCode":"404"}</p>
 * 
 * @author Jonse
 * @date 2020年10月16日
 */
public class ErrProtocol extends Protocol {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5096910125340922856L;
	
	private String event;//消息类型
	private String errorCode;
	private String errorMsg;
	
	private ErrProtocol() {
		
	}
	
	/** 创建一个错误的消息
	 * 
	 * @param errorCode
	 * @param errorMsg
	 * @param ch
	 * @return
	 */
	public static ErrProtocol newProtocol(String errorCode, String errorMsg, String ch) {
		ErrProtocol protocol = new ErrProtocol();
		protocol.setEvent(EvType.error.getEvent());
		protocol.setCh(ch);
		protocol.setErrorCode(errorCode);
		protocol.setErrorMsg(errorMsg);
		return protocol;
	}
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
