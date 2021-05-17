package com.sleuth.api.dto;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;

import com.sleuth.core.script.Fnbody;
import com.sleuth.core.script.Include;

public class ContractDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2473477648291328029L;
	
	private byte[] hashId;
	private String tappHash;// tapp的hash值
	
	private Fnbody fnbody;//函数内容
	private Include[] includes;//导入的其他函数
	private String jsscript;//重构后的JS脚本，可执行
	private String source;//源代码
	
	private Long createAt;// 合约创建时间(单位:秒)
	private Integer release;//是否发布0-未发布  1-已发布
	private Long releaseAt;// 发布时间(单位:秒)

	/** 转换为string的哈希代码
     * 
     * @return
     */
    public String getHexHash() {
    	return DigestUtils.sha256Hex(this.hashId);
    }
	
	public byte[] getHashId() {
		return hashId;
	}
	public void setHashId(byte[] hashId) {
		this.hashId = hashId;
	}
	public String getTappHash() {
		return tappHash;
	}
	public void setTappHash(String tappHash) {
		this.tappHash = tappHash;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	public Integer getRelease() {
		return release;
	}
	public void setRelease(Integer release) {
		this.release = release;
	}
	public Long getReleaseAt() {
		return releaseAt;
	}
	public void setReleaseAt(Long releaseAt) {
		this.releaseAt = releaseAt;
	}
	public Fnbody getFnbody() {
		return fnbody;
	}
	public void setFnbody(Fnbody fnbody) {
		this.fnbody = fnbody;
	}
	public Include[] getIncludes() {
		return includes;
	}
	public void setIncludes(Include[] includes) {
		this.includes = includes;
	}
	public String getJsscript() {
		return jsscript;
	}
	public void setJsscript(String jsscript) {
		this.jsscript = jsscript;
	}
	
}
