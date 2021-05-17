package com.sleuth.block;

import com.sleuth.block.dto.BlockDTO;

/** 区块验证区块和区块链的验证
 * 
<p>1、区块的数据结构语法上有效</p>
<p>2、验证工作量证明，区块头的哈希值小于目标难度（确认包含足够的工作量证明）</p>
<p>3、区块时间戳早于验证时刻未来两个小时（允许时间错误）</p>
<p>4、验证区块大小在长度限制之内，即看区块大小是否在设定范围之内。（BTC是数据区块体不能大于1M，隔离验证区块不能大于3M；BCH是区块不能大于32M；BSV现在是不能大于128M）</p>
<p>5、第一个交易（且只有第一个）是coinbase交易，即一个区块，矿工只能给自己奖励一次</p>
<p>6、验证区块内的交易并确保它们的有效性：验证MerkleRoot是否是由区块体中的交易得到的，即重构区块Merkle树得到的树根，看是否和区块头中的hashMerkleRoot值相等</p>
 * @author Jonse
 * @date 2021年1月30日
 */
public interface BlockVerify {
	
	/** 验证区块
	 * 
	 * @param block
	 * @return
	 */
	public abstract boolean verify(BlockDTO block);
	
	
}
