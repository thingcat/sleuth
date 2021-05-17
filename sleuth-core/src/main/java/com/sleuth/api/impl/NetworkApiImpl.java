package com.sleuth.api.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sleuth.api.NetworkApi;
import com.sleuth.api.dto.NodeDTO;
import com.sleuth.api.dto.TopologyDTO;
import com.sleuth.core.socket.server.CLI;

import io.netty.channel.Channel;

@Service
public class NetworkApiImpl implements NetworkApi {

	@Override
	public TopologyDTO getTopology() {
		TopologyDTO dto = new TopologyDTO();
		Channel channel = CLI.get();
		if (channel != null) {
			dto.setServer(this.valueOf(channel));
		}
		Iterator<Channel> iterator = CLI.iterator();
		if (iterator != null) {
			List<NodeDTO> list = new ArrayList<NodeDTO>();
			while(iterator.hasNext()) {
				Channel e = iterator.next();
				NodeDTO node = this.valueOf(e);
				list.add(node);
			}
			dto.setClients(list);
		}
		return dto;
	}
	
	private NodeDTO valueOf(Channel channel) {
		InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		NodeDTO node = new NodeDTO();
		node.setUri(host);
		node.setPort(port);
		return node;
	}

}
