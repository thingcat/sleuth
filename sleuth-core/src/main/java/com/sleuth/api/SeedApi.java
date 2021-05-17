package com.sleuth.api;

import java.util.List;

import com.sleuth.api.dto.NodeDTO;

/** 种子管理
 * 
 * @author Jonse
 * @date 2020年11月7日
 */
public interface SeedApi {
	
	/** 种子列表
	 * 
	 * @return
	 */
	public abstract List<NodeDTO> list();
	
	/** 导入种子
	 * 
	 * @param base64Code
	 */
	public abstract void imports(String jsonText);
	
	/** 同步列表
	 * 
	 */
	public abstract void sync();
	
}
