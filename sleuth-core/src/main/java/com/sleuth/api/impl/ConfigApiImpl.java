package com.sleuth.api.impl;

import java.util.Properties;

import org.springframework.stereotype.Service;

import com.sleuth.api.ConfigApi;
import com.sleuth.api.dto.ConfigDTO;
import com.sleuth.api.form.ConfigForm;
import com.sleuth.core.config.Config;
import com.sleuth.core.config.ConfigProperty;
import com.sleuth.core.config.DefaultConfig;

@Service
public class ConfigApiImpl implements ConfigApi {
	
	final Config config = DefaultConfig.getInstance();
	
	@Override
	public ConfigDTO get() {
		ConfigDTO dto = new ConfigDTO();
		
		dto.setStoreRoot(config.get(ConfigProperty.store_root));
		dto.setDb(config.get(ConfigProperty.store_db));
		
		dto.setOauthPath(config.get(ConfigProperty.auth_path));
		dto.setWsProtocol(config.get(ConfigProperty.socket_ws_protocol));
		dto.setWsHost(config.get(ConfigProperty.socket_ws_host));
		dto.setWsPath(config.get(ConfigProperty.socket_ws_path));
		dto.setWsServerPort(config.get(ConfigProperty.socket_ws_server_port));
		dto.setWsClientPort(config.get(ConfigProperty.socket_ws_client_port));
		
		return dto;
	}

	@Override
	public void update(ConfigForm form) {
		Properties properties = new Properties();
		properties.put(ConfigProperty.store_root, form.getStoreRoot());
		properties.put(ConfigProperty.store_db, form.getDb());
		
		properties.put(ConfigProperty.auth_path, form.getOauthPath());
		properties.put(ConfigProperty.socket_ws_protocol, form.getWsProtocol());
		properties.put(ConfigProperty.socket_ws_host, form.getWsHost());
		properties.put(ConfigProperty.socket_ws_path, form.getWsPath());
		properties.put(ConfigProperty.socket_ws_server_port, form.getWsServerPort());
		properties.put(ConfigProperty.socket_ws_client_port, form.getWsClientPort());
		
		this.config.set(properties);
		
	}

}
