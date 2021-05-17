package com.sleuth.api.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.api.TxOutputApi;
import com.sleuth.block.UTXOManager;
import com.sleuth.block.transaction.utxo.PayableTxOutput;

@Service
public class TxOutputApiImpl implements TxOutputApi {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private UTXOManager utxoManager;
	
	
	@Override
	public PayableTxOutput findPayableTxOutput(BigDecimal amount, String address) {
		return this.utxoManager.findPayableTxOutput(amount, address);
	}

}
