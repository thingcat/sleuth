package com.sleuth.network.message.protocol;

/** 返回协议格式
 * 
 * <p>数据返回格式：{"event":"table", "ch":"block/sync", "data":"json"}</p>
 * <p>订阅指令返回格式：{"event":"subscribe", "ch":"block/sync", "data":"json"}</p>
 * <p>取消订阅指令返回格式：{"event":"unsubscribe", "ch":"block/sync", "data":"json"}</p>
 * 
 * <p>错误返回格式：{"event":"error", "ch":"block/sync", "errorCode":"404"}</p>
 * 
 * @author Jonse
 * @date 2020年10月15日
 */
public enum EvType {
	
	subscribe("subscribe"),//请求订阅频道
	unsubscribe("unsubscribe"),//请求取消订阅频道
	apply("apply"),//主动申请数据同步
	table("table"),//数据推送
	ok("ok"),//完成某个指令的响应
	error("error");//错误响应
	
	private String event;
	
	EvType(String event) {
		this.event = event;
	}

	public String getEvent() {
		return event;
	}

}
