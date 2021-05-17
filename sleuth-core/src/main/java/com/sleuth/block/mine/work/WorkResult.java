package com.sleuth.block.mine.work;

import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.WebUri;

/** 挖矿工作结果
 * 
 * @author Jonse
 * @date 2021年2月2日
 */
public class WorkResult {
	
	private Block block;
	private String wxRoot;//WebURIRoot根节点
	private String txRoot;//交易订单默克尔树根节点
	
	private Transaction[] transactions;
	private WebUri[] webURIs;
	
	private PowResult powResult;
	
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}
	public String getWxRoot() {
		return wxRoot;
	}
	public void setWxRoot(String wxRoot) {
		this.wxRoot = wxRoot;
	}
	public String getTxRoot() {
		return txRoot;
	}
	public void setTxRoot(String txRoot) {
		this.txRoot = txRoot;
	}
	public PowResult getPowResult() {
		return powResult;
	}
	public void setPowResult(PowResult powResult) {
		this.powResult = powResult;
	}
	public Transaction[] getTransactions() {
		return transactions;
	}
	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
	}
	public WebUri[] getWebURIs() {
		return webURIs;
	}
	public void setWebURIs(WebUri[] webURIs) {
		this.webURIs = webURIs;
	}
	
}
