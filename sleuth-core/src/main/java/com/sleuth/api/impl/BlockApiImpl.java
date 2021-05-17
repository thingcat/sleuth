package com.sleuth.api.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.api.BlockApi;
import com.sleuth.api.dto.BlockDTO;
import com.sleuth.block.BlockBuffer;
import com.sleuth.block.BlockChainManager;
import com.sleuth.block.transaction.TxMerkleService;
import com.sleuth.core.utils.CopyUtil;

@Service
public class BlockApiImpl implements BlockApi {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int default_limit = 30;
	
	@Resource
	private BlockChainManager blockChainManager;
	@Resource
	private BlockBuffer blockBuffer;
	@Resource
	private TxMerkleService merkleService;
	
	@Override
	public BlockDTO find(String hash) {
		/*Block block = this.blockChainManager.getBlock(hash);
		BlockDTO dto = CopyUtil.copyProperty(block, BlockDTO.class);
		String merkleHash = block.getMerkleHash();
		TxMerkle merkle = this.merkleService.get(merkleHash);
		dto.setActs(merkle.getAxHashs().length);*/
		return null;
	}

	@Override
	public List<BlockDTO> findForHeight(String tappHash, long fromHeight, long toHeight) {
		/*List<Block> blocks = this.blockChainManager.getBlockForHeight(tappHash, fromHeight, toHeight);
		if (blocks != null) {
			return CopyUtil.copyList(blocks, BlockDTO.class);
		}*/
		return null;
	}

	@Override
	public List<BlockDTO> findBuffer(int page) {
		List<com.sleuth.block.dto.BlockDTO> list = this.blockBuffer.getBlocks(page, default_limit);
		if (list != null && list.size() > 0) {
			return CopyUtil.copyList(list, BlockDTO.class);
		}
		return null;
	}

	@Override
	public List<BlockDTO> findBuffer(String tappHash) {
		List<com.sleuth.block.dto.BlockDTO> list = this.blockBuffer.getBlocks(tappHash);
		if (list != null && list.size() > 0) {
			return CopyUtil.copyList(list, BlockDTO.class);
		}
		return null;
	}

}
