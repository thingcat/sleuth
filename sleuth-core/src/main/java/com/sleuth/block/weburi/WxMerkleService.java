package com.sleuth.block.weburi;

import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.network.message.protocol.TabProtocol;

/** 默克尔树
 * 
 * @author Jonse
 * @date 2021年5月14日
 */
public interface WxMerkleService {
	
	/** 保存默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void add(WxMerkle merkle);
	
	/** 获得默克尔树
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract WxMerkle get(String merkleRoot);
	
	/** 广播接收到的默克尔树
	 * 
	 * @param dto
	 * @return
	 */
	public TabProtocol doRecvFrom(WxMerkleDTO dto);

}
