package com.sleuth.block.dto;

import com.sleuth.block.schema.WxMerkle;
import com.sleuth.core.socket.message.DTO;

/** 交易默克尔树
 * 
 * @author Jonse
 * @date 2021年1月31日
 */
public class WxMerkleDTO extends DTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3870160515644266795L;
	
	private String hash;//默克尔树hash值
	private String[] wxIds;//WebURI
	private Long createAt;
	private boolean hasNext;//是否有下一组，解决交易HASH值过多的影响
	
	public WxMerkleDTO() {
		
	}

	public WxMerkleDTO(WxMerkle merkle) {
		this.hash = merkle.getHash();
		this.wxIds = merkle.getWxIds();
		this.createAt = merkle.getCreateAt();
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

	public boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
}
