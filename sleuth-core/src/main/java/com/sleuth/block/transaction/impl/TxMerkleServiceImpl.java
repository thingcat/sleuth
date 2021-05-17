package com.sleuth.block.transaction.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.store.TxMerkleStore;
import com.sleuth.block.transaction.TxMerkleBuffer;
import com.sleuth.block.transaction.TxMerkleService;
import com.sleuth.block.transaction.MerkleVerify;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;

@Service
public class TxMerkleServiceImpl implements TxMerkleService {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 交易hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	@Resource
	private TxMerkleStore store;
	@Resource
	private TxMerkleBuffer merkleBuffer;
	@Resource
	private MerkleVerify merkleVerify;
	
	@Override
	public void add(TxMerkle merkle) {
		String merkleHash = merkle.getHash();
		TxMerkle object = this.store.get(merkleHash);
		if (object == null) {
			this.store.add(merkle);
		}
	}
	
	@Override
	public TxMerkle get(String merkleHash) {
		return this.store.get(merkleHash);
	}

	@Override
	@Broadcast(ch=CmdType.TX_MERKLE_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(TxMerkleDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.TX_MERKLE_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}
	
}
