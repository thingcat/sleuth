package com.sleuth.block.mine;


/** 寻址器
 * 
 * @author Jonse
 * @date 2021年5月12日
 */
public interface SleuthAddresser {
	
	/** 启动寻址器
	 * 
	 * @param seeds
	 */
	public abstract void start(final String... seeds);
	
	/** 停止寻址器
	 * 
	 */
	public abstract void shutdown();
	
}
