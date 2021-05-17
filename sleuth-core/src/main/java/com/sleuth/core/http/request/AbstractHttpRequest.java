package com.sleuth.core.http.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.http.HttpFile;
import com.sleuth.core.http.HttpResult;

public abstract class AbstractHttpRequest {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final RequestConfig.Builder builder = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).setCookieSpec(CookieSpecs.DEFAULT);
	
	final RequestConfig requestConfig = builder.build();
	
	protected RequestConfig proxyConfig(HttpHost proxy) {
		if (proxy == null) {
			throw new RuntimeException("Proxy host con't null");
		}
		return builder.setProxy(proxy).build();
	}
	
	protected RequestConfig defaultConfig() {
		return requestConfig;
	}
	
	protected HttpResult get(RequestConfig requestConfig, String httpUrl) {
		return this.get(requestConfig, httpUrl, null);
	}

	protected HttpResult get(RequestConfig requestConfig, String httpUrl, Map<String, String> header) {
		HttpGet httpGet = new HttpGet(httpUrl);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpResult httpResult = new HttpResult();
		try {
			logger.debug("Http GET url = {}", httpUrl);
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			if (header != null && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			
			httpResult.setStatusCode(response.getStatusLine().getStatusCode());
			httpResult.setContent(content);
			httpResult.setContentType(entity.getContentType().getValue());
		} catch (Exception e) {
			logger.error("Http GET ERROR ", e);
			httpResult.setError(e.getMessage());
			httpResult.setStatusCode(500);
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return httpResult;
	}

	protected HttpResult post(RequestConfig requestConfig, String httpUrl, Map<String, String> headers, Map<String, String> maps) {
		logger.debug("Http POST url = {}", httpUrl);
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpPost.setHeader(entry.getKey(), entry.getValue());
		}
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			return sendHttpPost(requestConfig, httpPost);
		} catch (Exception e) {
			logger.error("Http POST UnsupportedEncoding error, url = {}", httpUrl);
			HttpResult httpResult = new HttpResult();
			httpResult.setError(e.getMessage());
			httpResult.setStatusCode(500);
			return httpResult;
		}
	}

	protected HttpResult post(RequestConfig requestConfig, String httpUrl, Map<String, String> maps) {
		logger.debug("Http POST url = {}", httpUrl);
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			return sendHttpPost(requestConfig, httpPost);
		} catch (UnsupportedEncodingException e) {
			logger.error("Http POST UnsupportedEncoding error, url = {}", httpUrl);
			HttpResult httpResult = new HttpResult();
			httpResult.setError(e.getMessage());
			httpResult.setStatusCode(500);
			return httpResult;
		}
	}

	private HttpResult sendHttpPost(RequestConfig requestConfig, HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpResult httpResult = new HttpResult();
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			
			httpResult.setStatusCode(response.getStatusLine().getStatusCode());
			httpResult.setContent(content);
			httpResult.setContentType(entity.getContentType().getValue());
		} catch (Exception e) {
			logger.error("Http POST ERROR ", e);
			httpResult.setError(e.getMessage());
			httpResult.setStatusCode(500);
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return httpResult;
	}

	protected HttpFile download(RequestConfig requestConfig, String httpUrl, String filePath) {
		logger.debug("Http download url = {}", httpUrl);
		HttpGet httpGet = new HttpGet(httpUrl);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		InputStream iStream = null;
		OutputStream fos = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpGet.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			iStream = entity.getContent();
			byte[] bytes = new byte[1024];
			fos = new FileOutputStream(filePath);
			int len = -1;
			while ((len = iStream.read(bytes)) != -1) {
				fos.write(bytes, 0, len);
			}
			fos.flush();
			File file = new File(filePath);
			HttpFile httpFile = new HttpFile();
			httpFile.setName(file.getName());
			httpFile.setFilePath(file.getAbsolutePath());
			httpFile.setSize(file.length());
			httpFile.setSuffix(file.getName().substring(file.getName().lastIndexOf(".") + 1));
			logger.debug("Http File = {}", httpFile.toString());
			return httpFile;
		} catch (Exception e) {
			logger.error("Http GET ERROR: {}", e.getMessage());
		} finally {

			try {
				// 关闭连接,释放资源
				if (response != null) {
					IOUtils.closeQuietly(response);
				}
				if (httpClient != null) {
					httpClient.close();
				}

				if (iStream != null) {
					IOUtils.closeQuietly(iStream);
				}

				if (fos != null) {
					IOUtils.closeQuietly(fos);
				}
			} catch (IOException e) {
				logger.error("Http GET Release resources ERROR ", e);
			}
		}
		return null;
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @return
	 */
	protected HttpResult upload(RequestConfig requestConfig, File file, String httpUrl) {
		return upload(requestConfig, file, httpUrl, null);
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param httpUrl
	 * @param params
	 * @return
	 */
	protected HttpResult upload(RequestConfig requestConfig, File file, String httpUrl, Map<String, String> params) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpPost httpPost = null;
		HttpResult httpResult = new HttpResult();
		try {
			// 创建httpPost
			logger.debug("Http upload url = {}", httpUrl);
			httpPost = new HttpPost(httpUrl);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.addBinaryBody("file", file);
			if (params != null) {
				// 创建参数队列
				for (String key : params.keySet()) {
					entityBuilder.addTextBody(key, params.get(key));
				}
			}
			httpPost.setEntity(entityBuilder.build());
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			
			httpResult.setStatusCode(response.getStatusLine().getStatusCode());
			httpResult.setContent(content);
			httpResult.setContentType(entity.getContentType().getValue());
		} catch (Exception e) {
			logger.error("Http POST ERROR ", e);
			httpResult.setError(e.getMessage());
			httpResult.setStatusCode(500);
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return httpResult;
	}

}
