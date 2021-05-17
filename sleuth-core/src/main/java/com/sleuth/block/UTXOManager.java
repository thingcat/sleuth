package com.sleuth.block;

import java.math.BigDecimal;

import com.sleuth.block.schema.Block;
import com.sleuth.block.transaction.utxo.PayableTxOutput;

/** UTXO 池管理
 * 
 * @author Jonse
 * @date 2021年4月16日
 */
public interface UTXOManager {
	
	/** 在指定的区块重构UTXO索引
	 * 
	 */
	public abstract void reIndex(Block block);
	
	/** 合并区块交易
	 * 
	 * <p>当新的区块产生时候，需要合并区块中的交易</p>
	 * 
	 * @param block
	 */
	public abstract void join(Block block);
	
	/** 回滚某个区块
	 * 
	 * <p>将UTXO里面关于该区块的输入输出以及UTXO移除</p>
	 * 
	 * @param block
	 */
	public abstract void rollback(Block block);

	/** 查找用户可支付的交易输出
	 * 
	 * @param amount
	 * @param address
	 * @return
	 */
	public abstract PayableTxOutput findPayableTxOutput(BigDecimal amount, String address);
	
	
}
