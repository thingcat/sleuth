/* 
 * 方式一：
 * 利用区块来合并出UTXO，每创建完一个区块，合并出该区块的UTXO
 * 
 * 方式二：
 * 按交易笔数来合并出UTXO，每完成一笔合法交易，合并出UTXO
 * 
 */

package com.sleuth.block.transaction.utxo;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxInput;
import com.sleuth.block.schema.TxOutput;

/** UTXO 池
 * 
 * <p>unspent(未花费) transaction(交易) outputs(交易输出)</p>
 * 
 * @author Jonse
 * @date 2021年4月14日
 */
public abstract class UTXOPool {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	//合并输入输出形成的UTXO池
	protected static final Map<String, TxOutput[]> UTXO = Maps.newConcurrentMap();
	//所有的交易输入
	private static final Map<String, TxInput[]> TX_INPUTS = Maps.newConcurrentMap();
	//所有的交易输出
	private static final Map<String, TxOutput[]> TX_OUTPUTS = Maps.newConcurrentMap();
	//所有被移除的交易输出
	private static final Map<String, TxOutput[]> RM_OUTPUTS = Maps.newConcurrentMap();
	
	/** 重构索引，全局更新
	 * 
	 */
	public abstract void reIndex();
	
	
	/** 从外部计算结果中合并
	 * 
	 * @param ioResult
	 */
	protected void join(IOResult ioResult) {
		TX_INPUTS.putAll(ioResult.getInputs());
		TX_OUTPUTS.putAll(ioResult.getOutputs());
		this.joinAll();
	}
	
	/** 合并一组交易的交易
	 * 
	 * @param actions
	 */
	protected void join(List<Transaction> transactions) {
		//寻找交易里面的交易
		if (transactions != null && transactions.size() > 0) {
			for (Transaction transaction : transactions) {
				if (transaction != null) {
					this.join(transaction);
				}
			}
		}
	}
	
	/** 回滚交易里面的交易输出
	 * 
	 * @param actions
	 */
	protected void rollback(List<Transaction> transactions) {
		//寻找交易里面的交易
		if (transactions != null && transactions.size() > 0) {
			for (Transaction transaction : transactions) {
				if (transaction != null) {
					String txId = transaction.getTxId();
					TX_INPUTS.remove(txId);//移除交易输入
					TX_OUTPUTS.remove(txId);//移除交易输出
					//恢复UTXO被删除的交易输出
					TxOutput[] rmTxOutputs = RM_OUTPUTS.get(txId);
					if (rmTxOutputs != null) {
						TxOutput[] txOutputs = UTXO.get(txId);
						if (txOutputs == null) {
							txOutputs = rmTxOutputs;
						} else {
							txOutputs = ArrayUtils.addAll(txOutputs, txOutputs);
						}
						UTXO.put(txId, txOutputs);
					}
				}
			}
		}
	}
	
	/** 合并一个交易
	 * 
	 * @param tx
	 */
	protected void join(Transaction tx) {
		String txId = tx.getTxId();
		//从交易中提取交易输入和交易输出
		TxInput[] txInputs = tx.getInputs();
		TxOutput[] txOutputs = tx.getOutputs();
		//从UTXO池中移除交易输入
		if (txInputs != null) {
			//从池中删除引用的交易输入
			for(int i=0; i<txInputs.length; i++) {
				//交易引用的输入（引用其他交易的输出）交易ID
				String iTxId = txInputs[i].getTxId();
				//从交易输出中找到第n笔交易
				TxOutput[] outputs = UTXO.get(iTxId);
				if (outputs != null) {
					//引用的第n笔交易输出，就是要移除的交易输出
					int outIndex = txInputs[i].getTxOutputIndex();
					outputs = ArrayUtils.remove(outputs, outIndex);
					if (outputs == null || outputs.length == 0) {
						//如果交易输出用完了，则移除掉
						UTXO.remove(iTxId);
					} else {
						UTXO.put(iTxId, outputs);
					}
					
					//记录被移除的交易输出-------------
					TxOutput[] rmTxOutputs = RM_OUTPUTS.get(iTxId);
					if (rmTxOutputs == null) {
						rmTxOutputs = new TxOutput[]{ outputs[outIndex] };
					} else {
						rmTxOutputs = ArrayUtils.add(rmTxOutputs, outputs[outIndex]);
					}
					//保存在缓存中
					RM_OUTPUTS.put(iTxId, rmTxOutputs);
				}
			}
		}
		//放入到UTXO池
		UTXO.put(tx.getTxId(), txOutputs);
		//放入到交易输入里面
		if (!TX_INPUTS.containsKey(txId)) {
			TX_INPUTS.put(txId, txInputs);
		}
		//放入到交易输出里面
		if (!TX_OUTPUTS.containsKey(txId)) {
			TX_OUTPUTS.put(txId, txOutputs);
		}
	}
	
	/** 合并所有的输入输出
	 * 
	 */
	private void joinAll() {
		UTXO.clear();
		Map<String, TxOutput[]> mapTxOutputs = Maps.newHashMap();
		mapTxOutputs.putAll(TX_OUTPUTS);
		if (TX_INPUTS != null && TX_INPUTS.size() > 0) {
			for(Map.Entry<String, TxInput[]> entry : TX_INPUTS.entrySet()) {
				TxInput[] txInputs = entry.getValue();
				for(int i=0; i<txInputs.length; i++) {
					//交易引用的输入（引用其他交易的输出），引用交易ID
					String txId = txInputs[i].getTxId();
					//从交易输出中找到第n笔交易
					TxOutput[] txOutputs = TX_OUTPUTS.get(txId);
					if (txOutputs != null) {
						//引用的第n笔交易输出，就是要移除的交易输出
						int outIndex = txInputs[i].getTxOutputIndex();
						//移除交易输出
						txOutputs = ArrayUtils.remove(txOutputs, outIndex);
						
						if (txOutputs == null || txOutputs.length == 0) {
							//如果交易输出用完了，则移除掉
							mapTxOutputs.remove(txId);
						} else {
							//重新放入UTXO中
							mapTxOutputs.put(txId, txOutputs);
						}
						
						//记录被移除的交易输出-------------
						TxOutput[] rmTxOutputs = RM_OUTPUTS.get(txId);
						if (rmTxOutputs == null) {
							rmTxOutputs = new TxOutput[]{ txOutputs[outIndex] };
						} else {
							rmTxOutputs = ArrayUtils.add(rmTxOutputs, txOutputs[outIndex]);
						}
						//保存在缓存中
						RM_OUTPUTS.put(txId, rmTxOutputs);
					}
				}
			}
		}
		UTXO.putAll(mapTxOutputs);
		mapTxOutputs.clear();
	}
	
}
