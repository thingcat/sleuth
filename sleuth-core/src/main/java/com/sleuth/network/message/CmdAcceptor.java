package com.sleuth.network.message;

import com.sleuth.network.message.protocol.ApProtocol;

import io.netty.channel.Channel;

/** 同步受理者
 * 
 * @author Jonse
 * @date 2020年11月16日
 */
public interface CmdAcceptor {
	
	/** 受理请求，并同步数据
	 * 
	 * @param channel
	 * @param protocol
	 */
	public abstract void onAccept(Channel channel, ApProtocol message);
	
}
