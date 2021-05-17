package com.sleuth.api;

import com.sleuth.api.dto.ConfigDTO;
import com.sleuth.api.form.ConfigForm;

public interface ConfigApi {
	
	/** 获取配置信息
	 * 
	 * @return
	 */
	public abstract ConfigDTO get();
	
	/** 更新配置信息
	 * 
	 * @param form
	 */
	public abstract void update(ConfigForm form);
	
	
}
