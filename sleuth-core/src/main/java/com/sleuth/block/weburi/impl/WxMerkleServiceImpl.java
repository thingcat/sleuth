package com.sleuth.block.weburi.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.store.WxMerkleStore;
import com.sleuth.block.transaction.MerkleVerify;
import com.sleuth.block.weburi.WxMerkleBuffer;
import com.sleuth.block.weburi.WxMerkleService;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;

@Service
public class WxMerkleServiceImpl implements WxMerkleService {
	
final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 交易hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	@Resource
	private WxMerkleStore store;
	@Resource
	private WxMerkleBuffer merkleBuffer;
	@Resource
	private MerkleVerify merkleVerify;
	
	@Override
	public void add(WxMerkle merkle) {
		String merkleRoot = merkle.getHash();
		WxMerkle object = this.store.get(merkleRoot);
		if (object == null) {
			this.store.add(merkle);
		}
	}

	@Override
	public WxMerkle get(String merkleRoot) {
		return this.store.get(merkleRoot);
	}

	@Override
	@Broadcast(ch=CmdType.WX_MERKLE_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(WxMerkleDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.WX_MERKLE_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}

}
