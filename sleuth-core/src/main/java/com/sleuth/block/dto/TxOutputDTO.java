package com.sleuth.block.dto;

import java.math.BigDecimal;

/** 交易输出
 * 
 * @author Jonse
 * @date 2018年11月2日
 */
public class TxOutputDTO {
	
	/** 数额大小
	 */
	private BigDecimal amount;//数额
	
	/** 通常是收款人公钥等组成的锁定脚本
	 */
	private String scriptPubKey;//公钥hash
	
	public TxOutputDTO() {
		
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getScriptPubKey() {
		return scriptPubKey;
	}

	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}

}
