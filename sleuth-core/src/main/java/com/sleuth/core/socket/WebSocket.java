package com.sleuth.core.socket;

public interface WebSocket {
	
	/** 启动网络服务
	 
	 * @return
	 */
	public abstract void service();
	
	/** 关闭网络服务
	 * 
	 */
	public abstract void destroy();
	
}
