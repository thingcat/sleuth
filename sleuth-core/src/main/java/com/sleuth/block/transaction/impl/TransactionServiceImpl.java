package com.sleuth.block.transaction.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.store.TransactionStore;
import com.sleuth.block.transaction.TransactionBuffer;
import com.sleuth.block.transaction.TransactionService;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.transaction.TxUtils;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.message.push.Broadcast;
import com.sleuth.network.message.push.Broadcast.Source;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TransactionStore store;
	@Resource
	private TransactionBuffer txBuffer;
	@Resource
	private TxMerkleManager merkleManager;
	
	@Override
	public void add(Transaction newTransaction) {
		Transaction action = this.store.get(newTransaction.getTxId());
		if (action == null) {
			this.store.add(newTransaction);
		}
	}
	
	@Override
	public void add(Transaction[] trans) {
		for (Transaction tx : trans) {
			this.add(tx);
		}
	}
	
	@Override
	public void add(TransactionDTO dto) {
		String txId = dto.getTxId();
		Transaction e = this.store.get(txId);
		if (e == null) {
			Transaction transaction = TxUtils.toSchema(dto);
			this.store.add(transaction);
		}
	}
	
	@Override
	public List<Transaction> findByMerkleHash(String merkleHash) {
		TxMerkle merkle = this.merkleManager.get(merkleHash);
		if (merkle != null) {
			String[] txIds = merkle.getTxIds();
			List<Transaction> list = new ArrayList<Transaction>(txIds.length);
			for (int i=0; i<txIds.length; i++) {
				list.add(this.store.get(txIds[i]));
			}
			return list;
		}
		return null;
	}

	@Override
	public Transaction findById(String axId) {
		return this.store.get(axId);
	}

	@Override
	public List<Transaction> findByPageable(String merkleHash, int page, int limit) {
		TxMerkle merkle = this.merkleManager.get(merkleHash);
		if (merkle != null) {
			List<String> list = Arrays.asList(merkle.getTxIds());
			int fromIndex = (page - 1) * limit;
			int toIndex = fromIndex + limit;
			int length = list.size();
			
			if (toIndex > length) {
				toIndex = length;
				if (fromIndex > toIndex) {
					return null;
				}
			}
			
			List<String> subListResult;
			try {
				subListResult = list.subList(fromIndex, toIndex);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
			
			List<Transaction> result = new ArrayList<Transaction>(subListResult.size());
			for (int i=0; i<subListResult.size(); i++) {
				result.add(this.store.get(subListResult.get(i)));
			}
			return result;
		}
		return null;
	}

	@Override
	@Broadcast(ch=CmdType.TRANSACTION_PUSH, ds=Source.local)
	public TabProtocol doProduce(Transaction transaction) {
		TransactionDTO dto = TxUtils.toDTO(transaction);
		TabProtocol message = TabProtocol.newProtocol(CmdType.TRANSACTION_PUSH);
		message.setData(JSON.toJSONString(dto));
		return message;
	}

	@Override
	@Broadcast(ch=CmdType.TRANSACTION_PUSH, ds=Source.push)
	public TabProtocol doRecvFrom(TransactionDTO dto) {
		TabProtocol message = TabProtocol.newProtocol(CmdType.TRANSACTION_PUSH);
		message.setData(JSON.toJSONString(dto));
		message.setUid(dto.getUid());
		return message;
	}
	
}
