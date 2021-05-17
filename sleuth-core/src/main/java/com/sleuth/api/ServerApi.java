package com.sleuth.api;

import javax.servlet.http.HttpServletRequest;

public interface ServerApi {
	
	public abstract void start(HttpServletRequest request);
	
	public abstract void reload(HttpServletRequest request);
	
	public abstract void stop(HttpServletRequest request);
	
}
