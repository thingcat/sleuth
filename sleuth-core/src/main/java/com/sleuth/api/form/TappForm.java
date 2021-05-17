package com.sleuth.api.form;

import java.io.Serializable;

import com.sleuth.core.web.annotation.Length;
import com.sleuth.core.web.annotation.NotNull;
import com.sleuth.core.web.annotation.Regex;

public class TappForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -630552665028564068L;

	@NotNull(message="args.defect")
	@Length(min=1, max=32, message="form.tapp.name.length")
	@Regex(regex="[\\u4e00-\\u9fa5]*|\\w*|\\d*|_*", message="form.tapp.name.regex")
	private String name;//应用名称
	
	@NotNull(message="args.defect")
	@Length(min=1, max=8192, message="form.tapp.icon.length")
	private String icon;//图标
	
	@NotNull(message="args.defect")
	@Length(min=1, max=8, message="form.tapp.bitsratio.length")
	@Regex(regex="^[0-9]*$", message="form.tapp.bitsratio.regex")
	private Integer bitsRatio;//目标挖矿难度系数
	
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
