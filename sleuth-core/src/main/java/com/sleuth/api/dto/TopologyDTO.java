package com.sleuth.api.dto;

import java.io.Serializable;
import java.util.List;

/** 网络拓扑结构
 * 
 * @author Jonse
 * @date 2020年11月24日
 */
public class TopologyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7421403572339658343L;

	private NodeDTO server;
	
	private List<NodeDTO> clients;
	
	public NodeDTO getServer() {
		return server;
	}
	public void setServer(NodeDTO server) {
		this.server = server;
	}
	public List<NodeDTO> getClients() {
		return clients;
	}
	public void setClients(List<NodeDTO> clients) {
		this.clients = clients;
	}
	
}
