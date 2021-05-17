package com.sleuth.block.transaction.utxo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.sleuth.block.schema.TxOutput;

/** 可支付的交易输出
 * 
 * @author Jonse
 * @date 2021年4月15日
 */
public class SpendableTxOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4398343451178085358L;
	
	/** 总金额 */
	private BigDecimal amount;
	
	/** 未花费的交易  <txId-index>*/
	private Map<String, int[]> spendable;
	
	/** 未花费的交易  <txId-index>*/
	private Map<String, TxOutput[]> txOutputs;
	
	public SpendableTxOutput() {
		
	}
	
	public SpendableTxOutput(BigDecimal amount, Map<String, int[]> spendable, Map<String, TxOutput[]> txOutputs) {
		this.amount = amount;
		this.spendable = spendable;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Map<String, int[]> getSpendable() {
		return spendable;
	}
	public void setSpendable(Map<String, int[]> spendable) {
		this.spendable = spendable;
	}

	public Map<String, TxOutput[]> getTxOutputs() {
		return txOutputs;
	}

	public void setTxOutputs(Map<String, TxOutput[]> txOutputs) {
		this.txOutputs = txOutputs;
	}
	
}
