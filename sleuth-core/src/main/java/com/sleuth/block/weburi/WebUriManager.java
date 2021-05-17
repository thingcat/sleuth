package com.sleuth.block.weburi;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;

public interface WebUriManager {
	
	public abstract void add(WebUri webUri);
	
	public abstract void add(WebUri[] webUris);
	
	/** 从池里面获取
	 * 
	 * @param wxId
	 * @return
	 */
	public abstract WebUri findById(String wxId);
	
	/** 打包当前的weburi
	 * 
	 * @param height
	 * @return
	 */
	public abstract WebUriPack baleBufferWebUri(Long height);
	
	/** 主动拉取的数据
	 * 
	 * @param dto
	 */
	public abstract void pullResult(WebUriDTO dto);
	
	/** 外部推送过来的数据
	 * 
	 * @param dto
	 */
	public abstract void pushResult(WebUriDTO dto);

}
