package com.sleuth.network.peer;

import io.netty.channel.Channel;

public interface AdapterMessage {
	
	public abstract void onAdapter(Channel channel, String msgText);

}
