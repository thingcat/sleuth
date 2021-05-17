package com.sleuth.core.http.request;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.http.HttpFile;
import com.sleuth.core.http.HttpRequest;
import com.sleuth.core.http.HttpResult;

/** 普通http请求
 * 
 * @author Jonse
 * @date 2021年1月14日
 */
public class DefaultHttpRequest extends AbstractHttpRequest implements HttpRequest {
	
	final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	@Override
	public HttpResult get(String httpUrl) {
		return this.get(httpUrl, null);
	}

	@Override
	public HttpResult get(String httpUrl, Map<String, String> header) {
		if (header == null) {
			return this.get(defaultConfig(), httpUrl);
		}
		return this.get(defaultConfig(), httpUrl, header);
	}

	@Override
	public HttpResult post(String httpUrl, Map<String, String> maps) {
		return this.post(httpUrl, null, maps);
	}
	
	@Override
	public HttpResult post(String httpUrl, Map<String, String> headers, Map<String, String> maps) {
		if (headers == null) {
			return this.post(defaultConfig(), httpUrl, maps);
		}
		return this.post(defaultConfig(), httpUrl, headers, maps);
	}

	@Override
	public HttpFile download(String httpUrl, String filePath) {
		return this.download(defaultConfig(), httpUrl, filePath);
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @return
	 */
	@Override
	public HttpResult upload(File file, String httpUrl) {
		return this.upload(file, httpUrl, null);
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @param params
	 * @return
	 */
	@Override
	public HttpResult upload(File file, String httpUrl, Map<String, String> params) {
		return this.upload(defaultConfig(), file, httpUrl, params);
	}

}
