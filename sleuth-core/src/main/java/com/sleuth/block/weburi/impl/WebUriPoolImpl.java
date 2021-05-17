package com.sleuth.block.weburi.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.service.Iterator;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WebUriPool;
import com.sleuth.block.weburi.WxMerkleManager;

@Service
public class WebUriPoolImpl implements WebUriPool {

	final Logger logger = LoggerFactory.getLogger(getClass());
	final Random random = new Random();
	
	static final Map<String, WebUri> WEB_URIS = Maps.newConcurrentMap();
	
	@Resource
	private BlockChain blockChain;
	@Resource
	private BlockService blockService;
	@Resource
	private WxMerkleManager merkleManager;
	
	@Resource
	private WebUriManager webUriManager;
	
	@Override
	public void onPreloading() {
		logger.warn("Loading WebURI pool..............");
		Iterator iterator = new Iterator(blockChain, blockService);
		while(iterator.hasNext()) {
			this.join(iterator.next());
		}
	}
	
	@Override
	public WebUri random() {
		int size = WEB_URIS.size();
		int i = this.random.nextInt(size);
		int temp = 0;
		for(Map.Entry<String, WebUri> entry : WEB_URIS.entrySet()) {
			if (i == temp) {
				return entry.getValue();
			}
		}
		Collection<WebUri> collection = WEB_URIS.values();
		WebUri[] webUris = collection.toArray(new WebUri[size]);
		//默认抽取最后一个
		return webUris[size-1];
	}

	@Override
	public WebUri get(String wxId) {
		return WEB_URIS.get(wxId);
	}

	@Override
	public void join(Block block) {
		String merkleRoot = block.getWxRoot();
		WxMerkle merkle = merkleManager.get(merkleRoot);
		if (merkle != null) {
			String[] wxIds = merkle.getWxIds();
			for (String wxId : wxIds) {
				WebUri webUri = webUriManager.findById(wxId);
				if (webUri != null) {
					WEB_URIS.put(wxId, webUri);
				}
			}
		}
	}

	@Override
	public void join(WebUri webUri) {
		if (!WEB_URIS.containsKey(webUri.getWxId())) {
			WEB_URIS.put(webUri.getWxId(), webUri);
		}
	}
	
}
