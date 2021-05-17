package com.sleuth.block.weburi;

import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;

public interface WxMerkleManager {
	
	/** 构建默克尔树
	 * 
	 * @param actions
	 * @return
	 */
	public abstract String builder(WebUri[] webUris);
	
	/** 校验默克尔树
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract boolean verify(WxMerkleDTO merkle);
	
	/** 保存本地生产的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void addResult(WxMerkle merkle);
	
	/** 保存接收到的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void pushResult(WxMerkleDTO merkle);
	
	/** 申请同步过来的默克尔树
	 * 
	 * @param merkle
	 */
	public abstract void pullResult(WxMerkleDTO merkle);
	
	/** 获取默克尔树信息
	 * 
	 * @param merkleHash
	 * @return
	 */
	public abstract WxMerkle get(String merkleRoot);
	
}
