package com.sleuth.block.weburi.impl;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.weburi.WxMerkleBuffer;

@Service
public class WxMerkleBufferImpl implements WxMerkleBuffer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	static final Map<String, WxMerkleDTO> maps = Maps.newConcurrentMap();

	@Override
	public WxMerkleDTO recvfrom(WxMerkleDTO dto) {
		//如果没有传输完成，则继续等待接收
		if (!dto.getHasNext()) {
			WxMerkleDTO merkle = maps.get(dto.getHash());
			if (merkle == null) {
				maps.put(dto.getHash(), dto);
			} else {
				//存在，则追加上去
				String[] wxIds = merkle.getWxIds();
				if (wxIds == null) {
					wxIds = dto.getWxIds();
				} else {
					wxIds = ArrayUtils.addAll(wxIds, dto.getWxIds());
				}
				merkle.setWxIds(wxIds);
			}
			return null;
		}
		
		//接收完成，寻找前面部分，是否存在
		WxMerkleDTO merkle = maps.get(dto.getHash());
		if (merkle != null) {
			//存在，则追加上去
			String[] wxIds = merkle.getWxIds();
			if (wxIds == null) {
				wxIds = dto.getWxIds();
			} else {
				wxIds = ArrayUtils.addAll(wxIds, dto.getWxIds());
			}
			merkle.setWxIds(wxIds);
		} else {
			merkle = dto;
		}
		return merkle;
	}

	@Override
	public void remove(String merkleRoot) {
		maps.remove(merkleRoot);
	}

}
