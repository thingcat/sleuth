package com.sleuth.block.transaction;

import java.util.List;

import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.network.message.protocol.TabProtocol;

/** 交易管理服务
 * 
 * @author Jonse
 * @date 2020年9月23日
 */
public interface TransactionService {
	
	/** 保存交易
	 * 
	 * @param action
	 */
	public abstract void add(Transaction tx);
	
	/** 保存交易
	 * 
	 * @param trans
	 */
	public abstract void add(Transaction[] trans);
	
	/** 保存交易，同步的时候使用
	 * 
	 * @param dto
	 */
	public abstract void add(TransactionDTO dto);
	
	/** 根据默克尔树获取区块里面的交易
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract List<Transaction> findByMerkleHash(String merkleHash);
	
	/** 获取交易
	 * 
	 * @param txId
	 * @return
	 */
	public abstract Transaction findById(String txId);
	
	/** 分页获取
	 * 
	 * @param merkleHash
	 * @param page
	 * @param limit
	 * @return
	 */
	public abstract List<Transaction> findByPageable(String merkleHash, int page, int limit);
	
	/** 广播本地生产的交易
	 * 
	 * @param block
	 * @return
	 */
	public TabProtocol doProduce(Transaction transaction);
	
	/** 广播接收到的交易
	 * 
	 * @param dto
	 * @return
	 */
	public TabProtocol doRecvFrom(TransactionDTO dto);
	
}
