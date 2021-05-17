package com.sleuth.block.schema;

/** 被移除的UTXO输出索引
 * 
 * @author Jonse
 * @date 2021年3月29日
 */
public class RmUTXO extends RmUTXOSchema {
	
	private String blockHash;//区块hash
	private String[] txIds;//被移除的交易ID
	
	public RmUTXO() {
		
	}
	
	public RmUTXO(String blockHash, String[] txIds) {
		this.blockHash = blockHash;
		this.txIds = txIds;
	}
	
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public String[] getTxIds() {
		return txIds;
	}
	public void setTxIds(String[] txIds) {
		this.txIds = txIds;
	}
	@Override
	public String toUnique() {
		return blockHash;
	}
	
}
