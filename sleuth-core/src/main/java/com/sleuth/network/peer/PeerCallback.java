package com.sleuth.network.peer;

import io.netty.channel.Channel;

/** 回调
 * 
 * @author Jonse
 * @date 2020年11月17日
 */
public interface PeerCallback {
	
	/** 回调
	 * 
	 * @param channel
	 */
	public abstract void handle(Channel channel);
	
}
