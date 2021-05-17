package com.sleuth.network.seed;

import java.util.List;

import com.sleuth.network.schema.NodeInfo;

/** 种子加载器
 * 
 * @author Jonse
 * @date 2020年9月26日
 */
public interface SeedLoader {

	/** 打开种子
	 * 
	 */
	public abstract void unpack();
	
	/** 打包种子
	 * 
	 * @return
	 */
	public abstract String pack(List<NodeInfo> nodeInfos);
	
}
