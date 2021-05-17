package com.sleuth.block.weburi.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.weburi.WebUriBuffer;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WebUriPack;
import com.sleuth.block.weburi.WebUriPool;
import com.sleuth.block.weburi.WebUriService;

@Service
public class WebUriManagerImpl implements WebUriManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private WebUriPool webUriPool;
	@Resource
	private WebUriBuffer webUriBuffer;
	@Resource
	private WebUriService webUriService;

	@Override
	public void add(WebUri webUri) {
		WebUri wx = this.webUriService.get(webUri.getWxId());
		if (wx == null) {
			this.webUriService.add(webUri);
		}
	}

	@Override
	public void add(WebUri[] webUris) {
		for (WebUri webUri : webUris) {
			this.add(webUri);
		}
	}
	
	@Override
	public WebUri findById(String wxId) {
		return this.webUriPool.get(wxId);
	}

	@Override
	public WebUriPack baleBufferWebUri(Long height) {
		return webUriBuffer.baleWebUri(height);
	}

	@Override
	public void pushResult(WebUriDTO dto) {
		//将事件加入到缓存中，等待被打包
		this.webUriBuffer.push(dto);
		//将事件广播出去
		this.webUriService.doRecvFrom(dto);
	}
	
	@Override
	public void pullResult(WebUriDTO dto) {
		this.webUriService.add(dto);
		this.webUriPool.join(new WebUri(dto));
	}

}
