package com.sleuth.block.service;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sleuth.block.UTXOManager;
import com.sleuth.block.schema.Block;
import com.sleuth.block.transaction.UTXOBuffer;
import com.sleuth.block.transaction.utxo.PayableTxOutput;
import com.sleuth.core.utils.Base58Check;

@Service
public class UTXOManagerImpl implements UTXOManager {
	
	@Resource
	private UTXOBuffer utxoBuffer;
	
	@Override
	public void reIndex(Block block) {
		this.utxoBuffer.init();
	}
	
	@Override
	public void join(Block block) {
		if (block != null) {
			this.utxoBuffer.combine(block);
		}
	}

	@Override
	public void rollback(Block block) {
		if (block != null) {
			this.utxoBuffer.rollback(block);
		}
	}

	@Override
	public PayableTxOutput findPayableTxOutput(BigDecimal amount, String address) {
		// 反向转化为 byte 数组
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        return this.utxoBuffer.findPayableTxOutput(amount, pubKeyHash);
	}

}
