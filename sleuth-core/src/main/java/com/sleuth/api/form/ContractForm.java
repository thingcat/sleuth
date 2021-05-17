package com.sleuth.api.form;

import com.sleuth.core.web.annotation.NotNull;

public class ContractForm {
	
	@NotNull(message="args.defect")
	private String tappHash;
	@NotNull(message="args.defect")
	private String script;
	
	public String getTappHash() {
		return tappHash;
	}
	public void setTappHash(String tappHash) {
		this.tappHash = tappHash;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}

}
