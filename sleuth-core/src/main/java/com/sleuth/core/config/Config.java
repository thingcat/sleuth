package com.sleuth.core.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 配置文件
 * 
 * @author Jonse
 * @date 2019年1月24日
 */
public abstract class Config {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String DEFAULT_CONFIG = "config.properties";
	static final Properties CONFIG_PROPER = new Properties();
	
	private String path;
	
	protected void read(String path) {
		if (path == null) {
			throw new RuntimeException(path + " was not found..........");
		}
		this.path = path;
		
		StringWriter writer = null;
		try {
			if (CONFIG_PROPER.size() < 1) {
				logger.info("Load config file on {}", path);
				CONFIG_PROPER.load(new FileReader(path));
				
				writer = new StringWriter();
				CONFIG_PROPER.store(writer, DEFAULT_CONFIG);
				logger.info(writer.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("An error occurred loading the configuration file..........");
		} finally {
			if (writer != null) {
				IOUtils.closeQuietly(writer);
			}
		}
	}
	
	public void reload() {
		if (path == null) {
			throw new RuntimeException(path + " was not found..........");
		}
		try {
			logger.info("Load config file on {}", path);
			CONFIG_PROPER.clear();
			CONFIG_PROPER.load(new FileReader(path));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("An error occurred loading the configuration file..........");
		}
		
	}
	
	public void set(Properties properties) {
		Set<Object> keySet = properties.keySet();
		for (Object key : keySet) {
			Object value = properties.get(key);
			if (value != null && !"".equals(value)) {
				this.put(key.toString(), value.toString());
			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(this.path));
			CONFIG_PROPER.store(fileWriter, "配置文件");
			logger.info("Successfully modified the configuration file in {}", this.path);
			
		} catch (IOException e) {
			logger.error("Configuration file modification failed.", e);
			throw new DefaultConfigException("Configuration file modification failed");
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}
	
	public String get(String key) {
		return CONFIG_PROPER.getProperty(key);
	}
	
	public String get(String key, String defValue) {
		String value = CONFIG_PROPER.getProperty(key);
		if (value != null) {
			return value;
		}
		return defValue;
	}
	
	public void put(String key, String value) {
		CONFIG_PROPER.put(key, value);
	}
	
	public Properties properties() {
		return CONFIG_PROPER;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
