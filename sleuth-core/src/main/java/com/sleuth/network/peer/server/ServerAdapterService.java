package com.sleuth.network.peer.server;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.core.socket.server.WebSocketServerAdapter;
import com.sleuth.network.peer.AdapterMessage;
import com.sleuth.network.peer.PeerCallback;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/** 服务端消息通道处理
 * 
 * @author Jonse
 * @date 2020年10月12日
 */
public class ServerAdapterService extends WebSocketServerAdapter {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final PeerCallback peerCallback;
	private final AdapterMessage adapterMessage;
	
	private final Timer timerCallback = new HashedWheelTimer();
	
	public ServerAdapterService(PeerCallback peerCallback, AdapterMessage adapterMessage) {
		this.peerCallback = peerCallback;
		this.adapterMessage = adapterMessage;
	}
	
	@Override
	public void onMessage(Channel channel, String msgText) {
		JSONObject jsonObject = JSON.parseObject(msgText);
		jsonObject.put("id", channel.id().asShortText());
		this.adapterMessage.onAdapter(channel, jsonObject.toJSONString());
	}

	@Override
	public void onSuccess(Channel channel) {
		this.timerCallback.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				peerCallback.handle(channel);
			}
		}, 5, TimeUnit.SECONDS);
	}
	
}
