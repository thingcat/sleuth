package com.sleuth.block.schema;

import com.sleuth.core.storage.annotation.Family;

/** 未花费的交易输出
 * 
 * <p>模型</p>
 * 
 * @author Jonse
 * @date 2021年3月29日
 */
@Family(name="output_utxo")
public class UTXO extends UTXOSchema {
	
	private String txId;
	private TxOutput[] txOutputs;
	
	public UTXO() {
		
	}
	
	public UTXO(String txId, TxOutput[] txOutputs) {
		this.txId = txId;
		this.txOutputs = txOutputs;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public TxOutput[] getTxOutputs() {
		return txOutputs;
	}

	public void setTxOutputs(TxOutput[] txOutputs) {
		this.txOutputs = txOutputs;
	}

	@Override
	public String toUnique() {
		return txId;
	}

}
