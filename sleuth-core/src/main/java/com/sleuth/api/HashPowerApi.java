package com.sleuth.api;

public interface HashPowerApi {
	
	/** 获取全网算力
	 * 
	 * @param tappHash
	 * @return
	 */
	public abstract long ghpCountValue(String tappHash);

}
