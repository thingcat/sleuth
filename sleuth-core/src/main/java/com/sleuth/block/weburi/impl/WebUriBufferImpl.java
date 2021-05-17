package com.sleuth.block.weburi.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.store.WebUriBufferStore;
import com.sleuth.block.subsidy.AllotRule;
import com.sleuth.block.weburi.WebUriBuffer;
import com.sleuth.block.weburi.WebUriPack;
import com.sleuth.block.weburi.WebUriVerify;
import com.sleuth.core.utils.Base64Util;

@Service
public class WebUriBufferImpl implements WebUriBuffer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final List<WebUri> WEB_URIS = Lists.newCopyOnWriteArrayList();
	
	final AllotRule allotRule = new AllotRule();
	
	@Resource
	private WebUriBufferStore store;
	@Resource
	private WebUriVerify webUriVerify;
	
	@Override
	public void onPreloading() {
		//加载缓存中的WebURI
		logger.info("loading WebURI buffer pool.");
		List<WebUri> result = this.store.list();
		if (result != null && result.size() > 0) {
			WEB_URIS.addAll(result);
		}
		logger.info("loading {} WebURI size.", this.size());
	}
	
	@Override
	public void push(WebUri webUri) {
		this.store.add(webUri);
		//加入内存中
		if (!WEB_URIS.contains(webUri)) {
			WEB_URIS.add(webUri);
		}
	}
	
	@Override
	public void push(WebUriDTO dto) {
		//验证
		if (this.webUriVerify.verify(dto)) {
			//加入内存中
			WebUri webUri = new WebUri(dto);
			if (!WEB_URIS.contains(new WebUri(dto))) {
				WEB_URIS.add(webUri);
			}
		}else {
			logger.warn("Invalid URI, txId = {}", dto.getWxId());
		}
	}
	
	@Override
	public WebUriPack baleWebUri(Long height) {
		int len = WEB_URIS.size();
		if (len > 0) {
			WebUriPack webUriPack = new WebUriPack();
			WebUri[] webUris = WEB_URIS.toArray(new WebUri[len]);
			Map<String, TxOutput> txOutputs = this.allotRule.compute(height, webUris);
			if (txOutputs != null && txOutputs.size() > 0) {
				Transaction[] transactions = {};
				for(Map.Entry<String, TxOutput> entry : txOutputs.entrySet()) {
					//锁定脚本，使用公钥hash作为锁定脚本
					byte[] scriptSign = Base64Util.toBinary(entry.getKey());
					TxOutput[] outputs = new TxOutput[]{ entry.getValue() };
					Transaction tx = Transaction.newCoinbaseTX(scriptSign, outputs);
					// 设置交易ID
					tx.setTxId(tx.toTxId());
					transactions = ArrayUtils.add(transactions, tx);
				}
				webUriPack.setTransactions(transactions);
			}
			webUriPack.setWebUris(webUris);
			return webUriPack;
		}
		return null;
	}

	@Override
	public long size() {
		return WEB_URIS.size();
	}

	@Override
	public WebUri findById(String wxId) {
		for(int i=0,size=WEB_URIS.size(); i<size; i++) {
			WebUri webUri = WEB_URIS.get(i);
			if (webUri.getWxId().equals(wxId)) {
				return webUri;
			}
		}
		return null;
	}

}
