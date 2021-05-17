package com.sleuth.block.service;

import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.schema.Block;

/** 区块链迭代器
 * 
 * @author Jonse
 * @date 2021年1月28日
 */
public class Iterator {
	
	private Long height;
	private String blockHash;
	
	private BlockChain blockChain;
	private BlockService service;
	
	public Iterator(BlockChain blockChain, BlockService service) {
		this.blockChain = blockChain;
		this.service = service;
		
		Block block = this.blockChain.currentBlock();
		this.height = block.getHeight();
		this.blockHash = block.getHash();
	}
	
	public Iterator(Block block, BlockChain blockChain, BlockService service) {
		this.blockChain = blockChain;
		this.service = service;
		this.height = block.getHeight();
		this.blockHash = block.getHash();
	}
	
	public boolean hasNext() {
		if (this.height.longValue() >= 0) {
			return true;
		}
		return false;
	}
	
	public Block next() {
		Block block = this.service.get(this.blockHash);
		this.height--;
		this.blockHash = block.getPrevBlockHash();
		return block;
	}
	
}
