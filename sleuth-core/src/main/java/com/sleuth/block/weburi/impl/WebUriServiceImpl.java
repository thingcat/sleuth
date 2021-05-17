package com.sleuth.block.weburi.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.store.WebUriStore;
import com.sleuth.block.weburi.WebUriService;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;

@Service
public class WebUriServiceImpl implements WebUriService {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private WebUriStore store;
	
	@Override
	public void add(WebUri webUri) {
		String wxId = webUri.getWxId();
		if (this.store.get(wxId) == null) {
			this.store.add(webUri);
		}
	}
	
	@Override
	public void add(WebUri[] webUris) {
		for (WebUri webUri : webUris) {
			this.add(webUri);
		}
	}
	
	public void add(WebUriDTO dto) {
		WebUri webUri = new WebUri(dto);
		this.add(webUri);
	}

	@Override
	public WebUri get(String wxId) {
		return this.store.get(wxId);
	}

	@Override
	@Broadcast(ch=CmdType.WEBURI_PUSH, ds=Source.local)
	public TabProtocol doProduce(WebUri webUri) {
		WebUriDTO dto = new WebUriDTO(webUri);
		TabProtocol message = TabProtocol.newProtocol(CmdType.WEBURI_PUSH);
		message.setData(JSON.toJSONString(dto));
		return message;
	}

	@Override
	@Broadcast(ch=CmdType.WEBURI_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(WebUriDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.WEBURI_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}
	
}
