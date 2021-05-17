package com.sleuth.network.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.dto.NodeInfoDTO;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.service.NodeInfoService;
import com.sleuth.network.store.NodeInfoStore;

@Service
public class NodeInfoServiceImpl implements NodeInfoService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private NodeInfoStore store;
	
	@Override
	public List<NodeInfo> list() {
		return this.store.list();
	}

	@Override
	public void add(NodeInfo nodeInfo) {
		if (nodeInfo != null && nodeInfo.getUri() != null) {
			this.store.set(nodeInfo);
			logger.debug("add node = {}", nodeInfo.toString());
		}
	}

	@Override
	@Broadcast(ch=CmdType.NODE_PUSH, ds=Source.local)
	public TabProtocol doProduce(NodeInfo nodeInfo) {
		//返回广播
		NodeInfoDTO data = CopyUtil.copyProperty(nodeInfo, NodeInfoDTO.class);
		TabProtocol message = TabProtocol.newProtocol(CmdType.NODE_PUSH);
		message.setData(JSON.toJSONString(data));
		return message;
	}

	@Override
	@Broadcast(ch=CmdType.NODE_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(NodeInfoDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.NODE_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}
	
}
