package com.sleuth.block.store;

import java.util.List;

import com.sleuth.block.schema.WebUri;

public interface WebUriStore {
	
	/** Save WebURI data
	 * 
	 * @param webURI
	 */
	public abstract void add(WebUri webURI);
	
	/** Get WebURI info.
	 * 
	 * @param wxId
	 * @return
	 */
	public abstract WebUri get(String wxId);
	
	/** find records based on pubKeyHash
	 * 
	 * @param pubKeyHash
	 * @return
	 */
	public abstract List<WebUri> getWebURIs(byte[] pubKeyHash);
	
}
