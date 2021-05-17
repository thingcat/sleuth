package com.sleuth.block.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.ChainRevise;
import com.sleuth.block.UTXOManager;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Chain;
import com.sleuth.block.store.ChainStore;
import com.sleuth.core.config.Genesis;

@Service
public class BlockChainImpl implements BlockChain, ChainRevise {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final Chain CURRENT_CHAIN = new Chain();
	
	@Resource
	private ChainStore store;
	@Resource
	private BlockService blockService;
	@Resource
	private UTXOManager utxoManager;
	
	@Override
	public void onPreloading() {
		Chain chain = this.store.get();
		if (chain != null) {
			CURRENT_CHAIN.setHeight(chain.getHeight());
			CURRENT_CHAIN.setBlockHash(chain.getBlockHash());
		} else {
			CURRENT_CHAIN.setHeight(Genesis.ZERO_HEIGHT);
			CURRENT_CHAIN.setBlockHash(Genesis.ZERO_BLOCK_HASH);
		}
		logger.warn("Current chain info = {}", CURRENT_CHAIN.toString());
	}
	
	@Override
	public synchronized void addToChain(Block newBlock) {
		Long height = newBlock.getHeight();
		String blockHash = newBlock.getHash();
		//如果比当前区块高度更高
		if (height > CURRENT_CHAIN.getHeight()) {
			CURRENT_CHAIN.setHeight(height);
			CURRENT_CHAIN.setBlockHash(blockHash);
			this.store.set(height, blockHash);
			//合并区块里面的交易
			this.utxoManager.join(newBlock);
		}
	}
	
	@Override
	public Block currentBlock() {
		String blockHash = CURRENT_CHAIN.getBlockHash();
		return this.blockService.get(blockHash);
	}

	@Override
	public void revise(Block block) {
		this.utxoManager.reIndex(block);
	}

}
