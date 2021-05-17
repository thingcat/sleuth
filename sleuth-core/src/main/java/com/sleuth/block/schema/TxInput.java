package com.sleuth.block.schema;

import java.util.Arrays;

import com.sleuth.core.utils.PublicKeyUtils;

/** 交易输入
 * 
 * @author Jonse
 * @date 2018年11月2日
 */
public class TxInput {
	
	private String txId;//引用交易Id的hash值
	private int txOutputIndex;//引用交易输出索引
	private byte[] scriptSign;//解锁脚本
	private byte[] sign;//签名
	
	public TxInput() {
		
	}
	
	private TxInput(String txId, int txOutputIndex, byte[] scriptSign, byte[] sign) {
		this.txId = txId;
		this.txOutputIndex = txOutputIndex;
		this.scriptSign = scriptSign;
		this.sign = sign;
	}
	
	public static TxInput newTxInput(String txId, int txOutputIndex, byte[] scriptSign, byte[] sign) {
		return new TxInput(txId, txOutputIndex, scriptSign, sign);
	}
	
    /**
     * 检查公钥hash是否用于交易输入
     *
     * @param pubKeyHash
     * @return
     */
    public boolean usesKey(byte[] pubKeyHash) {
        byte[] ripeMD160Hash = PublicKeyUtils.ripeMD160Hash(this.scriptSign);
        return Arrays.equals(ripeMD160Hash, pubKeyHash);
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

	public byte[] getSign() {
		return sign;
	}

	public void setSign(byte[] sign) {
		this.sign = sign;
	}

	public byte[] getScriptSign() {
		return scriptSign;
	}

	public void setScriptSign(byte[] scriptSign) {
		this.scriptSign = scriptSign;
	}
	
}
