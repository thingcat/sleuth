package com.sleuth.block.dto;

import com.sleuth.core.socket.message.DTO;

public class TransactionDTO extends DTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4415454985339913992L;
	
	private String txId;//交易Id
	private TxInputDTO[] inputs;//交易输入
	private TxOutputDTO[] outputs;//交易输出
	private Long createAt;//创建日期
	
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public TxInputDTO[] getInputs() {
		return inputs;
	}
	public void setInputs(TxInputDTO[] inputs) {
		this.inputs = inputs;
	}
	public TxOutputDTO[] getOutputs() {
		return outputs;
	}
	public void setOutputs(TxOutputDTO[] outputs) {
		this.outputs = outputs;
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

}
