package com.sleuth.context;

public interface ContextListener {
	
	/** 预加载，比onBeforeHandle更先加载
	 * 
	 */
	public abstract void onPreloading();
	
	/** 初始化
	 * 
	 */
	public abstract void onBeforeHandle();

	/** 销毁
	 * 
	 */
	public abstract void onAfterHandle();
	
}
