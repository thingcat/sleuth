package com.sleuth.network;

import com.sleuth.core.socket.WebSocket;

import io.netty.util.TimerTask;

/** 节点启动
 * 
 * @author Jonse
 * @date 2020年9月24日
 */
public interface PeerRunner extends TimerTask {

	/** 获得websocket信息
	 * @return
	 */
	public abstract WebSocket getSocket();
	
}
