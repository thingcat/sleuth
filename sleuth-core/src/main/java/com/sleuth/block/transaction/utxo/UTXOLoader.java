package com.sleuth.block.transaction.utxo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxInput;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.service.Iterator;
import com.sleuth.block.transaction.TransactionService;

/** UTXO加载器
 * 
 * @author Jonse
 * @date 2021年5月14日
 */
public class UTXOLoader {
	
	//所有的交易输入
	final Map<String, TxInput[]> mapTxInputs = Maps.newHashMap();
	//所有的交易输出
	final Map<String, TxOutput[]> mapTxOutputs = Maps.newHashMap();
	
	private BlockChain blockChain;
	private BlockService blockService;
	private TransactionService transactionService;
	
	public UTXOLoader(BlockChain blockChain, BlockService blockService, TransactionService transactionService) {
		this.blockChain = blockChain;
		this.blockService = blockService;
		this.transactionService = transactionService;
	}
	
	/** 执行加载任务
	 * 
	 * @return
	 */
	protected IOResult doLoading() {
		Iterator iterator = new Iterator(this.blockChain, this.blockService);
		//返回结果
		return this.forEach(iterator);
	}
	
	/** 执行加载任务
	 * 
	 * @return
	 */
	protected IOResult doLoading(Block block) {
		Iterator iterator = new Iterator(block, this.blockChain, this.blockService);
		//返回结果
		return this.forEach(iterator);
	}
	
	private IOResult forEach(Iterator iterator) {
		while(iterator.hasNext()) {
			Block block = iterator.next();
			if (block != null) {
				String merkleRoot = block.getTxRoot();
				//寻找区块里面的交易
				List<Transaction> transactions = this.transactionService.findByMerkleHash(merkleRoot);
				if (transactions != null && transactions.size() > 0) {
					for (Transaction tx : transactions) {
						if (isCoinbase(tx)) {
							mapTxOutputs.put(tx.getTxId(), tx.getOutputs());
						} else {
							//从交易中提取交易输入和交易输出
							TxInput[] txInputs = tx.getInputs();
							TxOutput[] txOutputs = tx.getOutputs();
							if (txInputs != null) {
								mapTxInputs.put(tx.getTxId(), txInputs);
							}
							mapTxOutputs.put(tx.getTxId(), txOutputs);
						}
					}
				}
			}
		}
		//返回结果
		return new IOResult(mapTxInputs, mapTxOutputs);
	}
	
	/**
     * 是否为 Coinbase 交易
     *
     * @return
     */
    private boolean isCoinbase(Transaction tx) {
        return tx.getInputs().length == 1
                && tx.getInputs()[0].getTxOutputIndex() == -1;
    }

}
