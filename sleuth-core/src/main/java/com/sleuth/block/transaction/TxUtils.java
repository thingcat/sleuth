package com.sleuth.block.transaction;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.dto.TxInputDTO;
import com.sleuth.block.dto.TxOutputDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxInput;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.core.utils.Base64Util;

public class TxUtils {
	
	/** 转换为存储对象
	 * 
	 * @param dto
	 * @return
	 */
	public static Transaction toSchema(TransactionDTO dto) {
		
		TxInputDTO[] inputs = dto.getInputs();
		if (inputs == null || inputs.length == 0) {
			return null;
		}
		
		TxOutputDTO[] outputs = dto.getOutputs();
		if (outputs == null || outputs.length == 0) {
			return null;
		}
		
		int len = inputs.length;
		TxInput[] txInputs = new TxInput[len];
		for(int i=0; i<len; i++) {
			txInputs[i] = new TxInput();
			txInputs[i].setTxId(inputs[i].getTxId());
			txInputs[i].setTxOutputIndex(inputs[i].getTxOutputIndex());
			txInputs[i].setScriptSign(Base64Util.toBinary(inputs[i].getScriptSign()));
			txInputs[i].setSign(Base64Util.toBinary(inputs[i].getSign()));
		}
		
		len = outputs.length;
		TxOutput[] txOutputs = new TxOutput[len];
		for(int i=0; i<len; i++) {
			txOutputs[i] = new TxOutput();
			txOutputs[i].setAmount(outputs[i].getAmount());
			txOutputs[i].setScriptPubKey(Base64Util.toBinary(outputs[i].getScriptPubKey()));
		}
		
		Transaction tx = new Transaction();
		tx.setTxId(dto.getTxId());
		tx.setInputs(txInputs);
		tx.setOutputs(txOutputs);
		tx.setCreateAt(dto.getCreateAt());
		return tx;
	}
	
	/** 转换为传输对象
	 * 
	 * @param action
	 * @return
	 */
	public static TransactionDTO toDTO(Transaction tx) {
		TxInput[] txInputs = tx.getInputs();
		if (txInputs == null || txInputs.length == 0) {
			//创币不给转换为传输对象
			return null;
		}
		
		int len = txInputs.length;
		TxInputDTO[] inputs = new TxInputDTO[len];
		for(int i=0; i<len; i++) {
			inputs[i] = new TxInputDTO();
			inputs[i].setTxId(txInputs[i].getTxId());
			inputs[i].setTxOutputIndex(txInputs[i].getTxOutputIndex());
			inputs[i].setScriptSign(Base64Util.toString(txInputs[i].getScriptSign()));
			inputs[i].setSign(Base64Util.toString(txInputs[i].getSign()));
		}
		
		TxOutput[] txOutputs = tx.getOutputs();
		len = txOutputs.length;
		TxOutputDTO[] outputs = new TxOutputDTO[len];
		for(int i=0; i<len; i++) {
			outputs[i] = new TxOutputDTO();
			outputs[i].setAmount(txOutputs[i].getAmount());
			outputs[i].setScriptPubKey(Base64Util.toString(txOutputs[i].getScriptPubKey()));
		}
		
		TransactionDTO dto = new TransactionDTO();
		dto.setTxId(tx.getTxId());
		dto.setInputs(inputs);
		dto.setOutputs(outputs);
		dto.setCreateAt(tx.getCreateAt());
		return dto;
	}

}
