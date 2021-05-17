package com.sleuth.block.transaction;

import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;

public interface TxMerkleManager {
	
	/** 构建默克尔树
	 * 
	 * @param actions
	 * @return
	 */
	public abstract String builder(Transaction[] transactions);
	
	/** 校验默克尔树
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract boolean verify(TxMerkleDTO merkle);
	
	/** 保存本地生产的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void addResult(TxMerkle merkle);
	
	/** 保存接收到的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void pushResult(TxMerkleDTO merkle);
	
	/** 申请同步过来的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void pullResult(TxMerkleDTO merkle);
	
	/** 获取默克尔树信息
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract TxMerkle get(String merkleHash);
	
}
