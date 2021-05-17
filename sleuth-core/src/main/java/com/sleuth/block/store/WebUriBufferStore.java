package com.sleuth.block.store;

import java.util.List;
import java.util.Set;

import com.sleuth.block.schema.WebUri;

public interface WebUriBufferStore {
	
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
	
	/** remove WebURI
	 * 
	 * @param wxId
	 */
	public abstract void remove(String wxId);
	
	/** remove WebURI
	 * 
	 * @param wxIds
	 */
	public abstract void remove(Set<String> wxIds);
	
	public abstract List<WebUri> list();
	
}
