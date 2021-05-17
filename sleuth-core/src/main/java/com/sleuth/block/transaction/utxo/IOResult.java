package com.sleuth.block.transaction.utxo;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sleuth.block.schema.TxInput;
import com.sleuth.block.schema.TxOutput;

public class IOResult {
	
	private Map<String, TxInput[]> inputs;
	private Map<String, TxOutput[]> outputs;
	
	public IOResult() {
		this.inputs = Maps.newHashMap();
		this.outputs = Maps.newHashMap();
	}
	
	public IOResult(Map<String, TxInput[]> inputs, Map<String, TxOutput[]> outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	public Map<String, TxInput[]> getInputs() {
		return this.inputs;
	}
	
	public Map<String, TxOutput[]> getOutputs() {
		return this.outputs;
	}
	
	public void addTxInput(String txId, TxInput[] txInputs) {
		if (!this.inputs.containsKey(txId)) {
			this.inputs.put(txId, txInputs);
		}
	}
	
	public void addTxOutput(String txId, TxOutput[] txOutputs) {
		if (!this.outputs.containsKey(txId)) {
			this.outputs.put(txId, txOutputs);
		}
	}

}
