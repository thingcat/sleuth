package com.sleuth.block.schema;

import com.sleuth.block.mine.work.WorkResult;
import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.DateUtils;

/** 交易默克尔树
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
@Family(name="merkle_weburi")
public class WxMerkle extends WxMerkleSchema {
	
	private String hash;//默克尔树hash值
	private String[] wxIds;//WebURI
	private Long createAt;
	
	public WxMerkle() {
		
	}
	
	/** 构建默克尔树对象
	 * 
	 * @param result
	 * @return
	 */
	public static WxMerkle newMerkle(WorkResult result) {
		return WxMerkle.newMerkle(result.getWxRoot(), result.getWebURIs());
	}
	
	/** 构建默克尔树对象
	 * 
	 * @param merkleHash
	 * @param transactions
	 * @return
	 */
	public static WxMerkle newMerkle(String merkleRoot, WebUri[] webURIs) {
		int size = webURIs.length;
		String[] items = new String[webURIs.length];
		for(int i=0; i<size; i++) {
			items[i] = webURIs[i].getWxId();
		}
		WxMerkle merkle = new WxMerkle();
		merkle.setWxIds(items);
		merkle.setHash(merkleRoot);
		merkle.setCreateAt(DateUtils.nowToUtc());
		return merkle;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String[] getWxIds() {
		return wxIds;
	}

	public void setWxIds(String[] wxIds) {
		this.wxIds = wxIds;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	@Override
	public String toUnique() {
		return this.getHash();
	}
	
}
