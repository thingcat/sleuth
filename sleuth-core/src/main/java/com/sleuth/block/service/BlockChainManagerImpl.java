package com.sleuth.block.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockChainManager;
import com.sleuth.block.BlockOrphan;
import com.sleuth.block.BlockService;
import com.sleuth.block.BlockVerify;
import com.sleuth.block.ChainRevise;
import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.mine.work.WorkResult;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.core.storage.annotation.Transactional;
import com.sleuth.core.utils.CopyUtil;

@Service
public class BlockChainManagerImpl implements BlockChainManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BlockVerify blockVerify;
	@Resource
	private BlockService blockService;
	@Resource
	private BlockChain blockChain;
	@Resource
	private ChainRevise chainRevise;
	@Resource
	private BlockOrphan blockOrphan;
	
	@Resource
	private TxMerkleManager txMerkleManager;
	@Resource
	private WxMerkleManager wxMerkleManager;
	@Resource
	private TransactionManager transactionManager;
	
	@Override
	@Transactional
	public void addResult(WorkResult result) {
		Block newBlock = result.getBlock();
		//1、保存区块信息
		this.blockService.add(newBlock);
		//2、保存链路结构
		this.blockChain.addToChain(newBlock);
		//3、保存默克尔树
		this.wxMerkleManager.addResult(WxMerkle.newMerkle(result));
		this.txMerkleManager.addResult(TxMerkle.newMerkle(result));
		//4、将缓存中的交易转移到正式库
		this.transactionManager.baleTransfer(result.getTransactions());
		//将区块广播出去
		this.blockService.doProduce(newBlock);
	}

	@Override
	@Transactional
	public void pushResult(BlockDTO dto) {
		Block newBlock = CopyUtil.copyProperty(dto, Block.class);
		Block block = this.blockService.get(dto.getHash());
		if (block == null) {
			//1、保存区块信息
			this.blockService.add(newBlock);
			//2、保存链路结构
			this.blockChain.addToChain(newBlock);
			//将区块再次广播出去
			this.blockService.doRecvFrom(dto);
		}
	}
	
	@Override
	@Transactional
	public void pullResult(BlockDTO dto) {
		Block block = CopyUtil.copyProperty(dto, Block.class);
		//1、保存区块信息
		this.blockService.add(block);
		//2、保存链路结构
		this.blockChain.addToChain(block);
	}
	
	@Override
	public boolean verify(BlockDTO block) {
		return this.blockVerify.verify(block);
	}

	@Override
	public boolean isOrphanBlock(BlockDTO block) {
		return this.blockOrphan.isOrphanBlock(block);
	}

	@Override
	public void addOrphanBlock(BlockDTO block) {
		this.blockOrphan.addOrphanBlock(block);
	}

	@Override
	public boolean checkMerkle(BlockDTO block) {
		String txRoot = block.getTxRoot();
		TxMerkle merkle = this.txMerkleManager.get(txRoot);
		return merkle != null;
	}

	@Override
	public Block getBlock(String hash) {
		return this.blockService.get(hash);
	}

	@Override
	public Block currentBlock() {
		return this.blockChain.currentBlock();
	}

}
