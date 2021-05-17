package com.sleuth.network.message.protocol;

public enum Op {

	sync("sync"),
	push("push");
	
	private String op;
	
	Op(String op) {
		this.op = op;
	}

	public String getOp() {
		return op;
	}
	
}
