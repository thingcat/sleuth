package com.sleuth.network.message.protocol;

/** 指令类型
 * 
 * @author Jonse
 * @date 2020年10月14日
 */
public enum CmdType {
	
	NODE_SYNC("node/sync"),//节点申请 server->client
	NODE_PUSH("node/push"),//节点同步 - 广播
	
	BLOCK_SYNC("block/sync"),//区块申请 server->client
	BLOCK_PUSH("block/push"),//区块同步 - 广播
	
	WEBURI_SYNC("weburi/sync"),//同步
	WEBURI_PUSH("weburi/push"),//广播
	
	TRANSACTION_SYNC("transaction/sync"),//交易申请 server->client
	TRANSACTION_PUSH("transaction/push"),//交易同步 - 广播
	
	TX_MERKLE_SYNC("tx_merkle/sync"),
	TX_MERKLE_PUSH("tx_merkle/push"),
	
	WX_MERKLE_SYNC("wx_merkle/sync"),
	WX_MERKLE_PUSH("wx_merkle/push");
	
	private String ch;
	private String mod;
	private String op;
	
	CmdType(String e) {
		String[] items = e.split("/");
		if (items.length == 1) {
			this.op = e;
		} else {
			this.ch = e;
			this.mod = items[0];
			this.op = items[1];
		}
	}

	public String getCh() {
		return ch;
	}

	public String getMod() {
		return mod;
	}

	public String getOp() {
		return op;
	}
	
	public static CmdType valueOf2(String e) {
		CmdType[] chTypes = CmdType.values();
		for (CmdType chType : chTypes) {
			String ch = chType.getCh();
			if (ch == null) {
				if (chType.getOp().equals(e)) {
					return chType;
				}
			} else {
				if (chType.getCh().equals(e)) {
					return chType;
				}
			}
		}
		return null;
	}
	
	public static CmdType valueOf2(Op op) {
		CmdType[] chTypes = CmdType.values();
		String e = op.getOp();
		for (CmdType chType : chTypes) {
			String ch = chType.getCh();
			if (ch == null) {
				if (chType.getOp().equals(e)) {
					return chType;
				}
			} else {
				if (chType.getCh().equals(e)) {
					return chType;
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		CmdType ch = CmdType.valueOf2("merkle/push");
		System.out.println(ch.getOp());
	}
	
}
