package com.sleuth.block.schema;

import java.util.Arrays;
import java.util.Objects;

import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.DateUtils;
import com.sleuth.core.utils.HexUtil;
import com.sleuth.core.utils.SerializeUtils;

/** 交易
 * 
 * <p>一笔token交易由 交易输入 和 交易输出 组成</p>
 * 
 * @author Jonse
 * @date 2018年11月2日
 */
@Family(name="transaction")
public class Transaction extends TransactionSchema {
	
	private String txId;//交易Id
	private TxInput[] inputs;//交易输入
	private TxOutput[] outputs;//交易输出
	private Long createAt;//创建日期
	
	public Transaction() {
		
	}
	
	private Transaction(TxInput[] inputs, TxOutput[] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.createAt = DateUtils.nowToUtc();
	}
	
	/** 构建一个交易对象
	 * 
	 * @param axId
	 * @param inputs
	 * @param outputs
	 * @return
	 */
	public static Transaction newTransaction(TxInput[] inputs, TxOutput[] outputs) {
		return new Transaction(inputs, outputs);
	}
	
	/** 构建一个创币交易对象
	 * 
	 * @param scriptSign
	 * @param outputs
	 * @return
	 */
	public static Transaction newCoinbaseTX(byte[] scriptSign, TxOutput[] outputs) {
		// 创建交易输入
		TxInput txInput = TxInput.newTxInput(new String(), -1, scriptSign, null);
		return new Transaction(new TxInput[]{txInput}, outputs);
	}
	
	/**
     * 计算交易信息的Hash值
     *
     * @return
     */
    public String toTxId() {
        // 使用序列化的方式对Transaction对象进行深度复制
        byte[] serializeBytes = SerializeUtils.serialize(this);
        Transaction copyTx = (Transaction) SerializeUtils.deserialize(serializeBytes);
        copyTx.setTxId(new String());
        copyTx.setCreateAt(null);
        return HexUtil.objectToHex(copyTx);
    }
    
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public TxInput[] getInputs() {
		return inputs;
	}
	public void setInputs(TxInput[] inputs) {
		this.inputs = inputs;
	}
	public TxOutput[] getOutputs() {
		return outputs;
	}
	public void setOutputs(TxOutput[] outputs) {
		this.outputs = outputs;
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Transaction) {
			if(this == obj){
	            return true;//地址相等
	        }
			Transaction tx = (Transaction) obj;
			return tx.getTxId().equals(this.getTxId());
		}
		return false;
	}
	@Override
    public int hashCode() {
		return Objects.hash(this.txId);
	}
	@Override
	public String toUnique() {
		return this.toTxId();
	}

	@Override
	public String toString() {
		return "Transaction [txId=" + txId + ", inputs=" + Arrays.toString(inputs) + ", outputs="
				+ Arrays.toString(outputs) + ", createAt=" + createAt + "]";
	}
	
}
