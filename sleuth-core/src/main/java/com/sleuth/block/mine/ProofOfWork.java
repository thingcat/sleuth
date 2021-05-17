package com.sleuth.block.mine;

import com.sleuth.block.exception.MineInterruptException;
import com.sleuth.block.mine.work.PowResult;

/**
 * 工作证明
 * 
 * @author Jonse
 * @date 2019年5月25日
 */
public interface ProofOfWork {

	/** 开始计算区块
	 * 
	 * @return
	 */
	public abstract PowResult compute() throws MineInterruptException ;
	
	/** 中断挖矿过程
	 * 
	 */
	public abstract void interrupt();
	
}
