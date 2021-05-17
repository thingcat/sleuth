package com.sleuth.block.transaction;

import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.network.message.protocol.TabProtocol;

/** 默克尔树
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public interface TxMerkleService {
	
	/** 保存默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void add(TxMerkle merkle);
	
	/** 获得默克尔树
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract TxMerkle get(String merkleRoot);
	
	/** 广播接收到的默克尔树
	 * 
	 * @param dto
	 * @return
	 */
	public TabProtocol doRecvFrom(TxMerkleDTO dto);
	
}
