package com.sleuth.core.http;

import java.io.File;
import java.util.Map;

/**
 * HTTP操作
 * 
 * @author Administrator
 *
 */
public interface HttpRequest {

	/** get 请求
	 * 
	 * @param httpUrl
	 * @return
	 */
	public abstract HttpResult get(String httpUrl);

	/** get 请求
	 * 
	 * @param httpUrl
	 * @param header
	 * @return
	 */
	public abstract HttpResult get(String httpUrl, Map<String, String> header);

	/** post 请求
	 * 
	 * @param httpUrl
	 * @param maps
	 * @return
	 */
	public abstract HttpResult post(String httpUrl, Map<String, String> maps);
	
	/** post 请求
	 * 
	 * @param httpUrl
	 * @param headers
	 * @param maps
	 * @return
	 */
	public abstract HttpResult post(String httpUrl, Map<String, String> headers, Map<String, String> maps);

	/** 文件下载
	 * 
	 * @param httpUrl
	 * @param filePath
	 * @return
	 */
	public abstract HttpFile download(String httpUrl, String filePath);

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @return
	 */
	public abstract HttpResult upload(File file, String httpUrl);

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @param params
	 * @return
	 */
	public abstract HttpResult upload(File file, String httpUrl, Map<String, String> params);

}
