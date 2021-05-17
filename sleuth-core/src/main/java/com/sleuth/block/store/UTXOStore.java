package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.UTXO;

/** UTXO池保存
 * 
 * <p>需要从所有应用链中把所有的交易取出来</p>
 * 
 * @author Jonse
 * @date 2021年3月30日
 */
public interface UTXOStore {
	
	/** 保存输出结果
	 * 
	 * @param txId 交易ID
	 * @param txOutputs 交易输出
	 */
	public abstract void add(String txId, TxOutput[] txOutputs);
	
	/** 获取输出结果
	 * 
	 * @param txId 交易ID
	 * @return
	 */
	public abstract UTXO get(String txId);
	
	/** 移除输出结果
	 * 
	 * @param axId 交易ID
	 */
	public abstract void remove(String txId);
	
	/** 根据公钥hash找寻输出记录
	 * 
	 * @param pubKeyHash
	 * @return
	 */
	public abstract List<UTXO> getTxOutputs(byte[] pubKeyHash);

}
