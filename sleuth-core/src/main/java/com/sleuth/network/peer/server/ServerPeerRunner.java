package com.sleuth.network.peer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.WebSocket;
import com.sleuth.network.PeerRunner;

import io.netty.util.Timeout;

/** 服务端节点启动
 * 
 * @author Jonse
 * @date 2020年9月24日
 */
public class ServerPeerRunner implements PeerRunner {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final WebSocket webSocket;
	
	public ServerPeerRunner(WebSocket webSocket) {
		this.webSocket = webSocket;
	}
	
	@Override
	public void run(Timeout timeout) {
		logger.info(">>>>>Start the peer websocket service");
		webSocket.service();
	}

	@Override
	public WebSocket getSocket() {
		return webSocket;
	}
	
}
