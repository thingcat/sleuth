package com.sleuth.block.transaction.utxo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.transaction.TransactionService;
import com.sleuth.block.transaction.UTXOBuffer;

@Service
public class UTXOBufferImpl extends UTXOPool implements UTXOBuffer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BlockChain blockChain;
	@Resource
	private BlockService blockService;
	@Resource
	private TransactionService transactionService;
	
	@Override
	public void init() {
		this.reIndex();
	}
	
	@Override
	public void reIndex() {
		logger.info("ReIndex UTXO.......");
		try {
			UTXOLoader utxoLoader = new UTXOLoader(blockChain, blockService, transactionService);
			IOResult ioResult = utxoLoader.doLoading();
			this.join(ioResult);
		} catch (Exception e) {
			logger.error("failed to reIndex UTXO........", e);
			throw new UTXOLoaderException("failed to reIndex UTXO.");
		}
		logger.info("UTXO reIndex successfully........");
	}
	
	@Override
	public void reIndex(Block block) {
		logger.info("ReIndex UTXO from block, hash = {}", block.getHash());
		try {
			UTXOLoader utxoLoader = new UTXOLoader(blockChain, blockService, transactionService);
			IOResult ioResult = utxoLoader.doLoading(block);
			this.join(ioResult);
		} catch (Exception e) {
			logger.error("failed to reIndex UTXO from block, hash = {}........", block.getHash(), e);
			throw new UTXOLoaderException("failed to reIndex UTXO, hash=" + block.getHash());
		}
		logger.info("UTXO reIndex successfully from block, hash = {}........", block.getHash());
	}
	
	@Override
	public void combine(Block block) {
		//根据默克尔树寻找区块里面的交易输出
		String merkleRoot = block.getTxRoot();
		List<Transaction> transactions = this.transactionService.findByMerkleHash(merkleRoot);
		logger.debug("Find {} transactions and add the transaction to UTXO pool", transactions.size());
		this.join(transactions);
	}
	
	@Override
	public void combine(Transaction tx) {
		this.join(tx);
	}
	
	@Override
	public void rollback(Block block) {
		//根据默克尔树寻找区块里面的交易输出
		String merkleRoot = block.getTxRoot();
		List<Transaction> transactions = this.transactionService.findByMerkleHash(merkleRoot);
		logger.debug("Find {} transactions and remove the transaction from UTXO pool", transactions.size());
		this.rollback(transactions);
	}
	
	@Override
	public PayableTxOutput findPayableTxOutput(BigDecimal amount, byte[] pubKeyHash) {
		BigDecimal accumulated = new BigDecimal("0");
		Map<String, int[]> spendable = Maps.newHashMap();
		Map<String, TxOutput[]> spendableTxOutputs = Maps.newHashMap();
		for(Map.Entry<String, TxOutput[]> entry : UTXO.entrySet()) {
			String txId = entry.getKey();//交易ID
			TxOutput[] txOutputs = entry.getValue();//交易的输出
			for(int outId=0; outId<txOutputs.length; outId++) {
				if (txOutputs[outId].isLockedWithKey(pubKeyHash)) {
					accumulated = accumulated.add(txOutputs[outId].getAmount());
					int[] outIds = spendable.get(txId);
					if (outIds == null) {
						outIds = new int[]{outId};
					} else {
						outIds = ArrayUtils.add(outIds, outId);
					}
					
					TxOutput[] spendTxOutputs = spendableTxOutputs.get(txId);
					if (spendTxOutputs == null) {
						spendTxOutputs = new TxOutput[]{txOutputs[outId]};
					} else {
						spendTxOutputs = ArrayUtils.add(spendTxOutputs, txOutputs[outId]);
					}
					
					spendable.put(txId, outIds);
					spendableTxOutputs.put(txId, spendTxOutputs);
					//找到了能够支付的金额，退出
					if (accumulated.compareTo(amount) >= 0) {
						break;
					}
				}
			}
		}
		return new PayableTxOutput(accumulated, spendable, spendableTxOutputs);
	}

	@Override
	public SpendableTxOutput findUnspendableTxOutput(byte[] pubKeyHash) {
		BigDecimal accumulated = new BigDecimal("0");
		Map<String, int[]> spendable = Maps.newHashMap();
		Map<String, TxOutput[]> spendableTxOutputs = Maps.newHashMap();
		for(Map.Entry<String, TxOutput[]> entry : UTXO.entrySet()) {
			String txId = entry.getKey();//交易ID
			TxOutput[] txOutputs = entry.getValue();//交易的输出
			for(int outId=0; outId<txOutputs.length; outId++) {
				if (txOutputs[outId].isLockedWithKey(pubKeyHash)) {
					accumulated = accumulated.add(txOutputs[outId].getAmount());
					int[] outIds = spendable.get(txId);
					if (outIds == null) {
						outIds = new int[]{outId};
					} else {
						outIds = ArrayUtils.add(outIds, outId);
					}
					
					TxOutput[] spendTxOutputs = spendableTxOutputs.get(txId);
					if (spendTxOutputs == null) {
						spendTxOutputs = new TxOutput[]{txOutputs[outId]};
					} else {
						spendTxOutputs = ArrayUtils.add(spendTxOutputs, txOutputs[outId]);
					}
					
					spendable.put(txId, outIds);
					spendableTxOutputs.put(txId, spendTxOutputs);
				}
			}
		}
		return new SpendableTxOutput(accumulated, spendable, spendableTxOutputs);
	}
	
}
