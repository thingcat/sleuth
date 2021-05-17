package com.sleuth.block.transaction.buffer;

import java.util.Arrays;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxInput;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.transaction.TransactionService;
import com.sleuth.block.transaction.TransactionVerify;
import com.sleuth.block.transaction.TxUtils;
import com.sleuth.core.utils.DateUtils;

@Service
public class TransactionVerifyImpl implements TransactionVerify {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TransactionService transactionService;
	
	@Override
	public boolean verify(TransactionDTO dto) {
		Transaction tx = TxUtils.toSchema(dto);
		if (!verifyTime(tx)) {
			logger.warn("time verify failed. txId = {}", tx.getTxId());
			return false;
		}
		
		if (!verifyTxInput(tx)) {
			logger.warn("TxInput verify failed. txId = {}", tx.getTxId());
			return false;
		}
		
		return true;
	}
	
	/** 交易时间戳检测
	 * 
	 *  交易的生成时间必须小于或者等于未来2个小时时间
	 *  
	 * @param block
	 * @return
	 */
	private boolean verifyTime(Transaction tx) {
		Long blockTime = tx.getCreateAt();
		Long localTime = DateUtils.nowToUtc() + 2 * 3600 * 1000;
		if (blockTime <= localTime) {
			return true;
		}
		logger.warn("Transaction timestamp too far in the future, txId={}", tx.getTxId());
		return false;
	}
	
	/** 验证交易输入
	 * 
	 * @param tx
	 * @return
	 */
	private boolean verifyTxInput(Transaction transaction) {
		TxInput[] txInputs = transaction.getInputs();
		if (txInputs == null || txInputs.length < 1) {
			return false;
		}
		
		for (TxInput txInput : txInputs) {
			//找出引用的交易
			Transaction inputTransaction = this.transactionService.findById(txInput.getTxId());
			if (inputTransaction == null) {
				return false;
			}
			//根据引用的索引，找出对应的交易输出
			int txOutIndex = txInput.getTxOutputIndex();
			TxOutput txOutput = inputTransaction.getOutputs()[txOutIndex];
			if (txOutput == null) {
				return false;
			}
			//判断解锁脚本是否相等，目前只作公钥验证
			if (!Arrays.equals(txInput.getScriptSign(), txOutput.getScriptPubKey())) {
				return false;
			}
		}
		return true;
	}
	
}
