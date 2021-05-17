package com.sleuth.block.schema;

import java.util.Objects;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.core.storage.annotation.Family;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.core.utils.HexUtil;
import com.sleuth.core.utils.SerializeUtils;

@Family(name = "web_uri")
public class WebUri extends WebUriSchema {

	private String wxId;//计算ID
	private String domain;//域名
	private String title;//标题
	private String uri;//地址
	private Long createAt;//创建日期
	private byte[] pubKeyHash;//公钥hash
	
	public WebUri() {
		
	}
	
	public WebUri(WebUriDTO webUri) {
		this.wxId = webUri.getWxId();
		this.domain = webUri.getDomain();
		this.title = webUri.getTitle();
		this.uri = webUri.getUri();
		this.createAt = webUri.getCreateAt();
		this.pubKeyHash = Base64Util.toBinary(webUri.getPubKeyHash());
	}
	
	public String toWxId() {
		// 创建信息的副本
		byte[] serializeBytes = SerializeUtils.serialize(this);
		WebUri copyWebURI = (WebUri) SerializeUtils.deserialize(serializeBytes);
		copyWebURI.setWxId(new String());//设置hash值
		copyWebURI.setUri(null);
		copyWebURI.setTitle(null);
		copyWebURI.setCreateAt(null);//时间戳不参与hash计算
//		copyWebURI.setPubKeyHash(null);//公钥hash不参与hash计算
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

	public byte[] getPubKeyHash() {
		return pubKeyHash;
	}

	public void setPubKeyHash(byte[] pubKeyHash) {
		this.pubKeyHash = pubKeyHash;
	}
	@Override
    public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof WebUri) {
			if(this == obj){
	            return true;//地址相等
	        }
			WebUri webUri = (WebUri) obj;
			return webUri.getWxId().equals(this.getWxId());
		}
		return false;
	}
	@Override
    public int hashCode() {
		return Objects.hash(this.wxId);
	}
	@Override
	public String toUnique() {
		return this.wxId;
	}

	@Override
	public String toString() {
		return "WebUri [wxId=" + wxId + ", domain=" + domain + ", title=" + title + ", uri=" + uri + ", createAt="
				+ createAt + "]";
	}

}
