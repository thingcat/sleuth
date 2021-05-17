package com.sleuth.block.weburi;

import com.sleuth.block.dto.WxMerkleDTO;

public interface WxMerkleBuffer {
	
	/** 接收数据
	 * 
	 * @param merkle
	 * @return
	 */
	public abstract WxMerkleDTO recvfrom(WxMerkleDTO dto);
	
	/** 移除缓存
	 * 
	 * @param merkleHash
	 */
	public abstract void remove(String merkleRoot);

}
