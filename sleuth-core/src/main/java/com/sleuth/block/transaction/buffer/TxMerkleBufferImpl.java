package com.sleuth.block.transaction.buffer;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.transaction.TxMerkleBuffer;

@Service
public class TxMerkleBufferImpl implements TxMerkleBuffer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 交易hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	static final Map<String, TxMerkleDTO> maps = Maps.newConcurrentMap();
	
	@Override
	public TxMerkleDTO recvfrom(TxMerkleDTO dto) {
		//如果没有传输完成，则继续等待接收
		if (!dto.getHasNext()) {
			TxMerkleDTO merkle = maps.get(dto.getHash());
			if (merkle == null) {
				maps.put(dto.getHash(), dto);
			} else {
				//存在，则追加上去
				String[] txIds = merkle.getTxIds();
				if (txIds == null) {
					txIds = dto.getTxIds();
				} else {
					txIds = ArrayUtils.addAll(txIds, dto.getTxIds());
				}
				merkle.setTxIds(txIds);
			}
			return null;
		}
		
		//接收完成，寻找前面部分，是否存在
		TxMerkleDTO merkle = maps.get(dto.getHash());
		if (merkle != null) {
			//存在，则追加上去
			String[] txIds = dto.getTxIds();
			if (txIds == null) {
				txIds = dto.getTxIds();
			} else {
				txIds = ArrayUtils.addAll(txIds, dto.getTxIds());
			}
			merkle.setTxIds(txIds);
		} else {
			merkle = dto;
		}
		return merkle;
	}

	@Override
	public void remove(String merkleHash) {
		maps.remove(merkleHash);
	}

}
