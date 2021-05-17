package com.sleuth.core.script.actuator;

import java.io.Serializable;

public class JsResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5488059009399748088L;
	
	private AxParams params;

	public AxParams getParams() {
		return params;
	}

	public void setParams(AxParams params) {
		this.params = params;
	}
	
}
