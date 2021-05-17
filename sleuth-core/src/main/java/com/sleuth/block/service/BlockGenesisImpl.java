package com.sleuth.block.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockGenesis;
import com.sleuth.block.schema.Block;
import com.sleuth.block.store.BlockStore;
import com.sleuth.block.transaction.TransactionService;
import com.sleuth.block.transaction.TxMerkleService;
import com.sleuth.block.weburi.WebUriService;
import com.sleuth.block.weburi.WxMerkleService;
import com.sleuth.core.config.CodingGenesisConfig;
import com.sleuth.core.config.Genesis;

@Service
public class BlockGenesisImpl implements BlockGenesis {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BlockStore store;
	@Resource
	private BlockChain blockChain;
	@Resource
	private TxMerkleService txMerkleService;
	@Resource
	private WxMerkleService wxMerkleService;
	@Resource
	private WebUriService webUriService;
	@Resource
	private TransactionService transactionService;

	@Override
	public void onPreloading() {
		final Genesis genesis = CodingGenesisConfig.getInstance();
		//保存创世区块信息
		Block genesisBlock = genesis.genesisBlock();
		if (genesisBlock != null) {
			Block block = this.store.get(genesisBlock.getHash());
			if (block == null) {
				//1、保存区块信息
				this.store.add(genesisBlock);
				//2、保存链路结构
				this.blockChain.addToChain(genesisBlock);
				logger.info("add the genesis block to the chain.");
			} else {
				logger.warn("the genesis block already exists, hash={}", block.getHash());
			}
		}
		
		this.webUriService.add(genesis.getWebURIs());
		this.transactionService.add(genesis.getTransactions());
		this.wxMerkleService.add(genesis.getWxMerkle());
		this.txMerkleService.add(genesis.getTxMerkle());
	}

	@Override
	public Block get() {
		final Genesis genesis = CodingGenesisConfig.getInstance();
		return genesis.genesisBlock();
	}

}
