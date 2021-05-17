package com.sleuth.api.form;

import java.io.Serializable;

public class ActionForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4657275063953878613L;

	private String axId;//事件Id
	private String tappHash;//Tapp哈希值
	private String version;//事件版本
	
	private String entries;//外部输入参数，json格式
	private String inputs;//引用其他事件的输出结果，json格式
	private String output;//事件输出成果，将成果输出到指定的公钥上，json格式
	private Long createAt;//创建日期
	
	public String getAxId() {
		return axId;
	}
	public void setAxId(String axId) {
		this.axId = axId;
	}
	public String getTappHash() {
		return tappHash;
	}
	public void setTappHash(String tappHash) {
		this.tappHash = tappHash;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEntries() {
		return entries;
	}
	public void setEntries(String entries) {
		this.entries = entries;
	}
	public String getInputs() {
		return inputs;
	}
	public void setInputs(String inputs) {
		this.inputs = inputs;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public Long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}
	
}
