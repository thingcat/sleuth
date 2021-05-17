package com.sleuth.block.weburi;

import com.sleuth.block.dto.WebUriDTO;

/** 校验规则
 * 
 * 1、是否被收录过，WebURI池里面
 * 2、是否在缓存中，等待被打包
 * 3、
 * 
 * @author Jonse
 * @date 2021年5月16日
 */
public interface WebUriVerify {
	
	public abstract boolean verify(WebUriDTO webUri);
	
}
