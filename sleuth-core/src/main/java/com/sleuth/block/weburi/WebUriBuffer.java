package com.sleuth.block.weburi;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;

public interface WebUriBuffer {
	
	public abstract void onPreloading();
	
	/** 加入缓冲池
	 * 
	 * @param webUri
	 * @return
	 */
	public abstract void push(WebUri webUri);
	
	/** 加入缓冲池
	 * 
	 * @param webUri
	 */
	public abstract void push(WebUriDTO webUri);
	
	/** 缓存池大小
	 * 
	 * @return
	 */
	public abstract long size();
	
	/** 将缓存池打包并转换为交易
	 * 
	 * @param hegith 需要打包的区块高度
	 * @return
	 */
	public abstract WebUriPack baleWebUri(Long height);
	
	public abstract WebUri findById(String txId);

}
