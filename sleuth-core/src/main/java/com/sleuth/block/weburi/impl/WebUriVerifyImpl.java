package com.sleuth.block.weburi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.weburi.WebUriVerify;
import com.sleuth.core.utils.DateUtils;

@Service
public class WebUriVerifyImpl implements WebUriVerify {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean verify(WebUriDTO dto) {
		WebUri webUri = new WebUri(dto);
		if (!verifyTime(webUri)) {
			logger.warn("time verify failed. wxId = {}", webUri.getWxId());
			return false;
		}
		
		return false;
	}
	
	/** 交易时间戳检测
	 * 
	 *  生成时间必须小于或者等于未来2个小时时间
	 *  
	 * @param block
	 * @return
	 */
	private boolean verifyTime(WebUri webUri) {
		Long blockTime = webUri.getCreateAt();
		Long localTime = DateUtils.nowToUtc() + 2 * 3600 * 1000;
		if (blockTime <= localTime) {
			return true;
		}
		logger.warn("WebUri timestamp too far in the future, wxId={}", webUri.getWxId());
		return false;
	}
	
}
