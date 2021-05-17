package com.sleuth.block.dto;

import com.sleuth.block.schema.WebUri;
import com.sleuth.core.socket.message.DTO;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.core.utils.HexUtil;
import com.sleuth.core.utils.SerializeUtils;

public class WebUriDTO extends DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3129969280239372517L;
	
	private String wxId;//计算ID
	private String domain;//域名
	private String title;//标题
	private String uri;//地址
	private Long createAt;//创建日期
	private String pubKeyHash;//公钥hash
	
	public WebUriDTO() {
		
	}
	
	public WebUriDTO(WebUri webUri) {
		this.wxId = webUri.getWxId();
		this.domain = webUri.getDomain();
		this.title = webUri.getTitle();
		this.uri = webUri.getUri();
		this.createAt = webUri.getCreateAt();
		this.pubKeyHash = Base64Util.toString(webUri.getPubKeyHash());
	}
	
	public String toWxId() {
		// 创建信息的副本
		byte[] serializeBytes = SerializeUtils.serialize(this);
		WebUri copyWebURI = (WebUri) SerializeUtils.deserialize(serializeBytes);
		copyWebURI.setWxId(new String());//设置hash值
		copyWebURI.setCreateAt(null);//时间戳不参与hash计算
		copyWebURI.setPubKeyHash(null);//公钥hash不参与hash计算
		return HexUtil.objectToHex(copyWebURI);
	}
	
	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getPubKeyHash() {
		return pubKeyHash;
	}

	public void setPubKeyHash(String pubKeyHash) {
		this.pubKeyHash = pubKeyHash;
	}

}
