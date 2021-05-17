package com.sleuth.block.transaction.buffer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.store.TransactionBufferStore;
import com.sleuth.block.transaction.TransactionBuffer;
import com.sleuth.block.transaction.TransactionVerify;
import com.sleuth.block.transaction.TxUtils;

@Service
public class TransactionBufferImpl implements TransactionBuffer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final List<Transaction> TRANSACTIONS = Lists.newCopyOnWriteArrayList();
	
	@Resource
	private TransactionBufferStore store;
	@Resource
	private TransactionVerify transactionVerify;
	
	@Override
	public void onPreloading() {
		//加载缓存中的交易
		logger.info("loading transaction buffer pool.");
		List<Transaction> result = this.store.list();
		if (result != null && result.size() > 0) {
			TRANSACTIONS.addAll(result);
		}
		logger.info("loading {} transaction size.", this.size());
	}
	
	@Override
	public void push(TransactionDTO dto) {
		//验证交易
		if (this.transactionVerify.verify(dto)) {
			//转换为存储交易对象
			Transaction transaction = TxUtils.toSchema(dto);
			if (this.store.get(dto.getTxId()) == null) {
				//保存到缓存库中
				this.store.add(transaction);
			}
			//加入内存中
			if (!TRANSACTIONS.contains(transaction)) {
				TRANSACTIONS.add(transaction);
			}
		} else {
			logger.warn("Invalid transaction, txId = {}", dto.getTxId());
		}
	}
	
	@Override
	public int size() {
		return TRANSACTIONS.size();
	}

	@Override
	public Transaction[] baleTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>(TRANSACTIONS);
		return transactions.toArray(new Transaction[transactions.size()]);
	}

	@Override
	public List<Transaction> getTransactions(int page, int limit) {
		if (TRANSACTIONS.size() > 0) {
			int fromIndex = (page - 1) * limit;
			int toIndex = fromIndex + limit;
			int length = TRANSACTIONS.size();
			if (toIndex > length) {
				toIndex = length;
				if (fromIndex > toIndex) {
					return null;
				}
			}
			try {
				return TRANSACTIONS.subList(fromIndex, toIndex);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public void remove(Transaction transaction) {
		String txId = transaction.getTxId();
		//从数据库中删除
		this.store.delete(txId);
		//从缓存中移除
		if (TRANSACTIONS != null && TRANSACTIONS.size() > 0) {
			int m = -1;
			for (int i = 0; i < TRANSACTIONS.size(); i++) {
				if (TRANSACTIONS.get(i).getTxId().equals(txId)) {
					m = i;
					break;
				}
			}
			if (m > -1) {
				TRANSACTIONS.remove(m);
			}
		}
	}
	
}
