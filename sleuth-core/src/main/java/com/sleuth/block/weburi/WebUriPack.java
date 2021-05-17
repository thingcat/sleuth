package com.sleuth.block.weburi;

import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.WebUri;

public class WebUriPack {
	
	private WebUri[] webUris;
	private Transaction[] transactions;
	
	public WebUri[] getWebUris() {
		return webUris;
	}
	public void setWebUris(WebUri[] webUris) {
		this.webUris = webUris;
	}
	public Transaction[] getTransactions() {
		return transactions;
	}
	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
	}

}
