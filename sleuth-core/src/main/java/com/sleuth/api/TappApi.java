package com.sleuth.api;

import java.util.List;

import com.sleuth.api.dto.TappDTO;
import com.sleuth.api.form.TappForm;

/** 应用管理
 * 
 * @author Jonse
 * @date 2020年11月9日
 */
public interface TappApi {
	
	/** 应用列表
	 * 
	 * @return
	 */
	public abstract List<TappDTO> list();
	
	public abstract TappDTO get(String id);
	
	public abstract void add(TappForm form);
	
}
