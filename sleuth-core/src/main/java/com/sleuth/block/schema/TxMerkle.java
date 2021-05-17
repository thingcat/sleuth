package com.sleuth.block.schema;

import com.sleuth.block.mine.work.WorkResult;
import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.DateUtils;

/** 交易默克尔树
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
@Family(name="merkle_transaction")
public class TxMerkle extends TxMerkleSchema {
	
	private String hash;//默克尔树hash值
	private String[] txIds;//交易IDs
	private Long createAt;
	
	public TxMerkle() {
		
	}
	
	/** 构建默克尔树对象
	 * 
	 * @param result
	 * @return
	 */
	public static TxMerkle newMerkle(WorkResult result) {
		return TxMerkle.newMerkle(result.getTxRoot(), result.getTransactions());
	}
	
	/** 构建默克尔树对象
	 * 
	 * @param merkleHash
	 * @param transactions
	 * @return
	 */
	public static TxMerkle newMerkle(String merkleRoot, Transaction[] transactions) {
		int size = transactions.length;
		String[] items = new String[transactions.length];
		for(int i=0; i<size; i++) {
			items[i] = transactions[i].getTxId();
		}
		TxMerkle merkle = new TxMerkle();
		merkle.setTxIds(items);
		merkle.setHash(merkleRoot);
		merkle.setCreateAt(DateUtils.nowToUtc());
		return merkle;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String[] getTxIds() {
		return txIds;
	}

	public void setTxIds(String[] txIds) {
		this.txIds = txIds;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	@Override
	public String toUnique() {
		return this.getHash();
	}
	
}
