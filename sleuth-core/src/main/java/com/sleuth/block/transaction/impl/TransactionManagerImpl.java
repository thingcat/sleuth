package com.sleuth.block.transaction.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.transaction.TransactionBuffer;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TransactionService;
import com.sleuth.block.transaction.TxUtils;
import com.sleuth.core.storage.annotation.Transactional;

@Service
public class TransactionManagerImpl implements TransactionManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TransactionBuffer transactionBuffer;
	
	@Resource
	private TransactionService transactionService;
	
	@Override
	public Transaction findById(String txId) {
		return this.transactionService.findById(txId);
	}
	
	@Override
	@Transactional
	public void addBuffer(TransactionDTO dto) {
		//将交易加入到缓存中
		this.transactionBuffer.push(dto);
		//将交易广播出去
		this.transactionService.doProduce(TxUtils.toSchema(dto));
	}
	
	@Override
	public void pushResult(TransactionDTO dto) {
		//将交易加入到缓存中
		this.transactionBuffer.push(dto);
		//将交易广播出去
		this.transactionService.doRecvFrom(dto);
	}
	
	@Override
	public void pullResult(TransactionDTO dto) {
		this.transactionService.add(dto);
	}
	
	@Override
	public Transaction[] baleBufferTransactions() {
		return this.transactionBuffer.baleTransactions();
	}

	@Override
	public void baleTransfer(Transaction[] transactions) {
		for (Transaction e : transactions) {
			this.transactionService.add(e);
			this.transactionBuffer.remove(e);
		}
	}

	@Override
	public void rmvBufferTransaction(Transaction e) {
		this.transactionBuffer.remove(e);
	}

	@Override
	public TransactionService getService() {
		return this.transactionService;
	}

	@Override
	public TransactionBuffer getBuffer() {
		return this.transactionBuffer;
	}
	
}
