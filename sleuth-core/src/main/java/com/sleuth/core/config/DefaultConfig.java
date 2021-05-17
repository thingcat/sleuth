package com.sleuth.core.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/** 默认配置文件
 * 
 * @author Jonse
 * @date 2019年1月24日
 */
public class DefaultConfig extends Config {
	
	static Config instance;
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new DefaultConfig();
		}
		return instance;
	}
	
	private DefaultConfig() {
		try {
			URL url = this.getClass().getResource("/");
			URI uri = new URI(url.toString() + DEFAULT_CONFIG);
			Path path = Paths.get(uri);
			this.read(path.toString());
		} catch (URISyntaxException e) {
			throw new DefaultConfigException();
		}
	}
	
}
