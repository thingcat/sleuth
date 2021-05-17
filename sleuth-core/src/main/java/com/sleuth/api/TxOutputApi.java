package com.sleuth.api;

import java.math.BigDecimal;

import com.sleuth.block.transaction.utxo.PayableTxOutput;

public interface TxOutputApi {
	
	/** 查询用户可支付的交易输出
	 * 
	 * @param amount
	 * @param address
	 * @return
	 */
	public abstract PayableTxOutput findPayableTxOutput(BigDecimal amount, String address);

}
