package com.sleuth.api;

import com.sleuth.api.dto.OAuthDTO;
import com.sleuth.api.form.KeyPairForm;

/** 用户身份
 * 
 * @author Jonse
 * @date 2020年11月1日
 */
public interface OAuthApi {
	
	/** 获得身份信息
	 * 
	 * @return
	 */
	public abstract OAuthDTO get();
	
	/** 导入新的身份信息
	 * 
	 * @param form
	 * @return
	 */
	public abstract OAuthDTO imports(KeyPairForm form);
	
}
