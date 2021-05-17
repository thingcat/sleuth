package com.sleuth.block.weburi;

import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.network.message.protocol.TabProtocol;

public interface WebUriService {
	
	public abstract void add(WebUri webUri);
	
	public abstract void add(WebUri[] webUris);
	
	public abstract void add(WebUriDTO dto);
	
	public abstract WebUri get(String wxId);
	
	/** 广播本地生产的数据
	 * 
	 * @param webUri
	 * @return
	 */
	public abstract TabProtocol doProduce(WebUri webUri);
	
	/** 广播外部接收到的数据
	 * 
	 * @param dto
	 * @return
	 */
	public abstract TabProtocol doRecvFrom(WebUriDTO dto);

	
}
