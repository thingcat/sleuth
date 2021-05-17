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
public class PayableTxOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4398343451178085358L;
	
	/** 交易时的支付金额 */
	private BigDecimal accumulated;
	
	/** 未花费的交易  <txId-index>*/
	private Map<String, int[]> spendable;
	
	/** 未花费的交易  <txId-index>*/
	private Map<String, TxOutput[]> txOutputs;
	
	public PayableTxOutput() {
		
	}
	
	public PayableTxOutput(BigDecimal accumulated, Map<String, int[]> spendable, Map<String, TxOutput[]> txOutputs) {
		this.accumulated = accumulated;
		this.spendable = spendable;
	}
	
	public BigDecimal getAccumulated() {
		return accumulated;
	}
	public void setAccumulated(BigDecimal accumulated) {
		this.accumulated = accumulated;
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
