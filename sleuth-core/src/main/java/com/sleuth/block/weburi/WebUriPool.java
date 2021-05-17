package com.sleuth.block.weburi;

import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.WebUri;

/** 从区块中加载所有的WebUri地址
 * 
 * @author Jonse
 * @date 2021年5月14日
 */
public interface WebUriPool {
	
	/** 加载WebUri池
	 * 
	 */
	public abstract void onPreloading();
	
	/** 随机获得一个URI
	 * 
	 * @return
	 */
	public abstract WebUri random();
	
	/** 从池里面获得记录
	 * 
	 * @param wxId
	 * @return
	 */
	public abstract WebUri get(String wxId);
	
	/** 合并新区块产生的WebUri
	 * 
	 * @param block
	 */
	public abstract void join(Block block);
	
	public abstract void join(WebUri webUri);
	
}
