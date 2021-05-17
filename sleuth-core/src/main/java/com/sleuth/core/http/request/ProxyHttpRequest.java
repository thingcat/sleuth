package com.sleuth.core.http.request;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.http.HttpFile;
import com.sleuth.core.http.HttpRequest;
import com.sleuth.core.http.HttpResult;

/** 代理请求
 * 
 * @author Jonse
 * @date 2021年1月13日
 */
public class ProxyHttpRequest extends AbstractHttpRequest implements HttpRequest {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String host;
	private int port;
	
	private final RequestConfig requestConfig;
	
	public ProxyHttpRequest(String host, int port) {
		this.host = host;
		this.port = port;
		
		RequestConfig.Builder builder = RequestConfig.custom();
		HttpHost proxy = new HttpHost(host, Integer.valueOf(port));
		builder.setProxy(proxy);
		requestConfig = builder.setSocketTimeout(15000).setConnectTimeout(15000)
				.setConnectionRequestTimeout(15000).setCookieSpec(CookieSpecs.DEFAULT).build();
	}
	
	@Override
	public HttpResult get(String httpUrl) {
		return this.get(requestConfig, httpUrl);
	}

	@Override
	public HttpResult get(String httpUrl, Map<String, String> header) {
		return this.get(requestConfig, httpUrl, header);
	}

	@Override
	public HttpResult post(String httpUrl, Map<String, String> maps) {
		return this.post(requestConfig, httpUrl, maps);
	}

	@Override
	public HttpResult post(String httpUrl, Map<String, String> headers, Map<String, String> maps) {
		return this.post(requestConfig, httpUrl, headers, maps);
	}

	@Override
	public HttpFile download(String httpUrl, String filePath) {
		return this.download(requestConfig, httpUrl, filePath);
	}

	@Override
	public HttpResult upload(File file, String httpUrl) {
		return this.upload(requestConfig, file, httpUrl);
	}

	@Override
	public HttpResult upload(File file, String httpUrl, Map<String, String> params) {
		return this.upload(requestConfig, file, httpUrl, params);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
