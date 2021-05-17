package com.sleuth.network.peer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.WebSocket;
import com.sleuth.network.PeerRunner;

import io.netty.util.Timeout;

/** 客户端节点
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public class ClientPeerRunner implements PeerRunner {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final WebSocket webSocket;
	
	public ClientPeerRunner(WebSocket webSocket) {
		this.webSocket = webSocket;
	}

	@Override
	public void run(Timeout timeout) {
		logger.info(">>>>>Start the client websocket service");
		this.webSocket.service();
	}

	@Override
	public WebSocket getSocket() {
		return this.webSocket;
	}

}
