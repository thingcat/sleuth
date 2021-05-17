package com.sleuth.block.schema;

import java.math.BigDecimal;
import java.util.Arrays;

import com.sleuth.core.utils.Base58Check;
import com.sleuth.core.utils.PublicKeyUtils;
import com.sleuth.oauth.keypair.KeyPairUtils;

/** 交易输出
 * 
 * @author Jonse
 * @date 2018年11月2日
 */
public class TxOutput {
	
	/** 数额大小
	 */
	private BigDecimal amount;//数额
	
	/** 通常是收款人公钥等组成的锁定脚本
	 */
	private byte[] scriptPubKey;//公钥hash
	
	public TxOutput() {
		
	}
	
	private TxOutput(BigDecimal amount, byte[] scriptPubKey) {
		this.amount = amount;
		this.scriptPubKey = scriptPubKey;
	}
	
	/**
     * 创建交易输出
     *
     * @param value
     * @param address
     * @return
     */
    public static TxOutput newTxOutput(BigDecimal amount, String address) {
        // 反向转化为 byte 数组
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        return new TxOutput(amount, pubKeyHash);
    }
    
    /**
     * 创建交易输出
     *
     * @param value
     * @param address
     * @return
     */
    public static TxOutput newTxOutput(BigDecimal amount, byte[] pubKeyHash) {
        return new TxOutput(amount, pubKeyHash);
    }

    /**
     * 检查交易输出是否能够使用指定的公钥
     *
     * @param pubKeyHash
     * @return
     */
    public boolean isLockedWithKey(byte[] pubKeyHash) {
        return Arrays.equals(this.scriptPubKey, pubKeyHash);
    }
    
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public byte[] getScriptPubKey() {
		return scriptPubKey;
	}

	public void setScriptPubKey(byte[] scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}

	public static void main(String[] args) {
		
		// 反向转化为 byte 数组
        byte[] versionedPayload = Base58Check.base58ToBytes("1Cvhtep6wRbronojezBV7QmZSfQAAszPZt");
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        System.out.println(KeyPairUtils.base64Encoded(pubKeyHash));
		
        byte[] publicKey = KeyPairUtils.base64Decode("BL0rD2fagg5IxJBCenMRO1jpzRc4mNsVlcGQ+F0LNLJE7Ew5K2ayDDF3wZcvg3vO9AlDif8qqPd2KvvGXPAPyJA=");
        String ripeMD160Hash = KeyPairUtils.base64Encoded(PublicKeyUtils.ripeMD160Hash(publicKey));
        System.out.println(ripeMD160Hash);
        
	}
	
}
