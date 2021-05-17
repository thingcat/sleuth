package com.sleuth.api.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sleuth.api.SeedApi;
import com.sleuth.api.dto.NodeDTO;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.core.web.exception.ResponseCodeException;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.service.NodeInfoService;

import io.netty.channel.Channel;

@Service
public class SeedApiImpl implements SeedApi {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private NodeInfoService nodeInfoService;
	
	@Override
	public List<NodeDTO> list() {
		List<NodeInfo> nodeInfos = nodeInfoService.list();
		return CopyUtil.copyList(nodeInfos, NodeDTO.class);
	}

	@Override
	public void sync() {
		Channel channel = CLI.get();
		if (channel == null) {
			throw new ResponseCodeException("api.send.sync.unconnect");
		}
	}

	@Override
	public void imports(String jsonText) {
		try {
			JSONArray jsonArray = JSON.parseArray(jsonText);
			if (jsonArray != null && jsonArray.size() > 0) {
				for(int i=0,size=jsonArray.size(); i<size; i++) {
					NodeInfo nodeInfo = jsonArray.getObject(i, NodeInfo.class);
					nodeInfoService.add(nodeInfo);
				}
			}
		} catch (Exception e) {
			throw new ResponseCodeException("api.send.imports.error");
		}
	}
	
}
