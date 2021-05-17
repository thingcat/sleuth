package com.sleuth.core.socket.client;

/** 回调事件
 * 
 * @author Administrator
 *
 */
public interface CallbackHandler {
	
	/** 回调
	 * 
	 * @param channel
	 */
	public abstract void handler(Object object);
	
}
