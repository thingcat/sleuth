package com.sleuth.block.store;

import com.sleuth.block.schema.RmUTXO;

/** 记录被删除的交易ID
 * 
 * @author Jonse
 * @date 2021年4月12日
 */
public interface RmUTXOStore {
	
	/** 保存UTXO中被删除的交易索引
	 * 
	 * @param rmUTXO
	 */
	public abstract void add(RmUTXO rmUTXO);
	
	/** 移除从UTXO中被删除的交易索引，说明已经被还原过了
	 * 
	 * @param blockHash
	 */
	public abstract void remove(String blockHash);
	
	/** 获取从UTXO中被删除的交易索引
	 * 
	 * @param blockHash
	 * @return
	 */
	public abstract RmUTXO get(String blockHash);
	
}
