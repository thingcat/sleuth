package com.sleuth.core.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Messages {
	
	static final Logger logger = LoggerFactory.getLogger(Messages.class);
	
	static final String ISO_8859_1 = "ISO-8859-1";
	static final String UTF_8 = "UTF-8";
	static final Map<String, Object> cache = new HashMap<String, Object>();
	
	public static void init(String config) {
		if (config != null) {
			try {
				Resource resource = new ClassPathResource(config);
				Properties properties = new Properties();
				properties.load(resource.getInputStream());
				set(properties);
			} catch (IOException e) {
				logger.error("加载消息配置文件出错：", e);
			}
		}
	}
	
	public static void set(Properties properties) {
		Set<Object> keySet = properties.keySet();
		if (keySet != null && keySet.size() > 0) {
			for (Object key : keySet) {
				String message = (String) properties.get(key);
				try {
					set((String)key, new String(message.getBytes(ISO_8859_1), UTF_8));
				} catch (UnsupportedEncodingException e) {
					logger.error("字符集转换错误，", e);
				}
			}
		}
	}
	
	public static void set(String key, Object value) {
		logger.debug("ADD message "+key+"="+value);
		cache.put(key, value);
	}
	
	public static String getString(String key) {
		Object message = cache.get(key);
		if (message == null || "".equals(message)) {
			return key;
		}
		return (String) message;
	}

}
