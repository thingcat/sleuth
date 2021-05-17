package com.sleuth.network.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.dto.NodeInfoDTO;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.service.NodeInfoManager;
import com.sleuth.network.service.NodeInfoService;

@Service
public class NodeInfoManagerImpl implements NodeInfoManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private NodeInfoService nodeInfoService;
	
	@Override
	public void add(NodeInfoDTO dto) {
		NodeInfo nodeInfo = CopyUtil.copyProperty(dto, NodeInfo.class);
		//保存节点信息
		this.nodeInfoService.add(nodeInfo);
		//广播节点
		this.nodeInfoService.doProduce(nodeInfo);
	}

	@Override
	public void pushResult(NodeInfoDTO dto) {
		NodeInfo nodeInfo = CopyUtil.copyProperty(dto, NodeInfo.class);
		//保存节点信息
		this.nodeInfoService.add(nodeInfo);
		//广播节点
		this.nodeInfoService.doRecvFrom(dto);
	}

	@Override
	public void pullResult(NodeInfoDTO dto) {
		NodeInfo nodeInfo = CopyUtil.copyProperty(dto, NodeInfo.class);
		//保存节点信息
		this.nodeInfoService.add(nodeInfo);
	}

}
