package com.sleuth.block.dto;

/** 交易输入
 * 
 * @author Jonse
 * @date 2018年11月2日
 */
public class TxInputDTO {
	
	private String txId;//引用交易Id的hash值
	private int txOutputIndex;//引用交易输出索引
	private String scriptSign;//解锁脚本
	private String sign;//签名
	
	public TxInputDTO() {
		
	}
	
	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public int getTxOutputIndex() {
		return txOutputIndex;
	}
	public void setTxOutputIndex(int txOutputIndex) {
		this.txOutputIndex = txOutputIndex;
	}

	public String getScriptSign() {
		return scriptSign;
	}

	public void setScriptSign(String scriptSign) {
		this.scriptSign = scriptSign;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
