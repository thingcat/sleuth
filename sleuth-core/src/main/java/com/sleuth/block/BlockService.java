package com.sleuth.block;

import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.schema.Block;
import com.sleuth.network.message.protocol.TabProtocol;

/** 区块服务
 * 
 * @author Jonse
 * @date 2021年2月7日
 */
public interface BlockService {
	
	/** 保存区块
	 * 
	 * @param block
	 */
	public abstract void add(Block block);
	
	/** 获取区块信息
	 * 
	 * @param hash
	 * @return
	 */
	public abstract Block get(String hash);
	
	/** 广播本地生产的区块
	 * 
	 * @param block
	 * @return
	 */
	public TabProtocol doProduce(Block block);
	
	/** 广播接收到的区块
	 * 
	 * @param dto
	 * @return
	 */
	public TabProtocol doRecvFrom(BlockDTO dto);
	
}
