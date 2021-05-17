package com.sleuth.api.dto;

import java.io.Serializable;

public class TappDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 147587658634691899L;
	
	private String hexHash;//tapp的hash值
	private String name;//应用名称
	private String icon;//图标base64字符
	private String tappCode;//随机代码
	private Integer bitsRatio;//目标挖矿难度系数
	private Integer load;//装载应用，默认不装载
	private Long createAt;// 应用创建时间(单位:毫秒)
	
	private long ghp;//全网算力
	private Long height;//最新高度
	private String lastHash;//最新高度对应的hash值
	private Long updateAt;//最后更新时间
	
	public String getHexHash() {
		return hexHash;
	}

	public void setHexHash(String hexHash) {
		this.hexHash = hexHash;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBitsRatio() {
		return bitsRatio;
	}

	public void setBitsRatio(Integer bitsRatio) {
		this.bitsRatio = bitsRatio;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public String getLastHash() {
		return lastHash;
	}

	public void setLastHash(String lastHash) {
		this.lastHash = lastHash;
	}

	public Long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Long updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getLoad() {
		return load;
	}

	public void setLoad(Integer load) {
		this.load = load;
	}

	public String getTappCode() {
		return tappCode;
	}

	public void setTappCode(String tappCode) {
		this.tappCode = tappCode;
	}

	public long getGhp() {
		return ghp;
	}

	public void setGhp(long ghp) {
		this.ghp = ghp;
	}

	
}
