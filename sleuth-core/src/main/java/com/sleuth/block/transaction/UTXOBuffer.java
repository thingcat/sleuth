package com.sleuth.block.transaction;

import java.math.BigDecimal;

import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.transaction.utxo.PayableTxOutput;
import com.sleuth.block.transaction.utxo.SpendableTxOutput;

/** UTXO 池管理
 * 
 * 在区块保存成功后才可进行操作，不能将之放在一个事务里面
 * 
 * @author Jonse
 * @date 2021年4月13日
 */
public interface UTXOBuffer {
	
	/** 初始化整个UTXO池
	 * 
	 * <p>遍历整个区块池，从所有的区块中获得</p>
	 * 
	 */
	public abstract void init();
	
	/** 从指定的区块中开始重构区块
	 * 
	 * @param block
	 */
	public abstract void reIndex(Block block);
	
	/** 合并区块交易
	 * 
	 * <p>当新的区块产生时候，需要合并区块中的交易</p>
	 * 
	 * @param block
	 */
	public abstract void combine(Block block);
	
	/** 合并一个交易
	 * 
	 * @param tx
	 */
	public abstract void combine(Transaction tx);
	
	/** 回滚某个区块
	 * 
	 * <p>将UTXO里面关于该区块的输入输出以及UTXO移除</p>
	 * 
	 * @param block
	 */
	public abstract void rollback(Block block);
	
	/** 查找用户能够支付的交易输出
	 * 
	 * @param amount
	 * @param pubKeyHash
	 * @return
	 */
	public abstract PayableTxOutput findPayableTxOutput(BigDecimal amount, byte[] pubKeyHash);
	
	/** 查找用户所有的 unspent(未花费) transaction(交易) outputs(交易输出)
	 * 
	 * @param pubKeyHash
	 * @return
	 */
	public abstract SpendableTxOutput findUnspendableTxOutput(byte[] pubKeyHash);
	
}
