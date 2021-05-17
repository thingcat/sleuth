package com.sleuth.block.transaction.utxo;

public class UTXOLoaderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2377264696862898687L;

	private String message;
	
	public UTXOLoaderException() {
		this.message = "UTXOLoader error";
	}
	
	public UTXOLoaderException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
