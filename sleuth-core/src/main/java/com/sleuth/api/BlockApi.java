package com.sleuth.api;

import java.util.List;

import com.sleuth.api.dto.BlockDTO;

public interface BlockApi {
	
	/** GET Block
	 * 
	 * @param hash
	 * @return
	 */
	public abstract BlockDTO find(String hash);
	
	public abstract List<BlockDTO> findForHeight(String tappHash, long fromHeight, long toHeight);
	
	public abstract List<BlockDTO> findBuffer(int page);
	
	public abstract List<BlockDTO> findBuffer(String tappHash);
	
}
