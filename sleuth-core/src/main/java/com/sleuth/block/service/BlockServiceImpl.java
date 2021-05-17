package com.sleuth.block.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.BlockOrphan;
import com.sleuth.block.BlockService;
import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.schema.Block;
import com.sleuth.block.store.BlockStore;
import com.sleuth.block.store.OrphanStore;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;

@Service
public class BlockServiceImpl implements BlockService, BlockOrphan {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BlockStore store;
	@Resource
	private OrphanStore orphanStore;
	
	@Override
	@Broadcast(ch=CmdType.BLOCK_PUSH, ds=Source.local)
	public TabProtocol doProduce(Block block) {
		BlockDTO dto = CopyUtil.copyProperty(block, BlockDTO.class);
		TabProtocol message = TabProtocol.newProtocol(CmdType.BLOCK_PUSH);
		message.setData(JSON.toJSONString(dto));
		return message;
	}

	@Override
	@Broadcast(ch=CmdType.BLOCK_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(BlockDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.BLOCK_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}
	
	@Override
	public void add(Block block) {
		if (this.get(block.getHash()) == null) {
			this.store.add(block);
		}
	}
	
	@Override
	public Block get(String hash) {
		return this.store.get(hash);
	}
	
	//----------------------------------------孤块管理

	@Override
	public void addOrphanBlock(BlockDTO dto) {
		if (this.orphanStore.get(dto.getHash()) == null) {
			Block block = CopyUtil.copyProperty(dto, Block.class);
			this.orphanStore.add(block);
			logger.warn("find Orphan block and store, hahs={}", block.getHash());
		}
	}

	@Override
	public List<Block> getOrphanBlocks() {
		return this.orphanStore.list();
	}

	@Override
	public void rmvOrphanBlock(Block block) {
		this.orphanStore.remove(block.getHash());
	}
	
	@Override
	public boolean isOrphanBlock(BlockDTO block) {
		Block prevBlock = this.get(block.getPrevBlockHash());
		return prevBlock == null;
	}

}
