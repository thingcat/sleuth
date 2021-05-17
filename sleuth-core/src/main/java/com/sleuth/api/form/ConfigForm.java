package com.sleuth.api.form;

import java.io.Serializable;

import com.sleuth.core.web.annotation.Enums;
import com.sleuth.core.web.annotation.Length;
import com.sleuth.core.web.annotation.NotNull;
import com.sleuth.core.web.annotation.Regex;

public class ConfigForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5166952739585279063L;
	
	@NotNull(message="args.defect")
	private String storeRoot;
	
	@NotNull(message="args.defect")
	@Regex(regex="^\\d{1}$", message="form.config.db.regex")
	private String db;
	
	@NotNull(message="args.defect")
	@Length(max=200, min=1, message="form.config.oauthpath.length")
	private String oauthPath;
	
	@NotNull(message="args.defect")
	@Enums(enums={"ws", "wss"}, message="form.config.wsprotocol.enums")
	private String wsProtocol;
	
	@NotNull(message="args.defect")
	@Length(max=200, min=1, message="form.config.wshost.length")
	@Regex(regex="(\\w*\\.?){3}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co|io)$", message="form.config.wshost.regex")
	private String wsHost;
	
	@NotNull(message="args.defect")
	@Length(max=20, min=1, message="form.config.wspath.length")
	@Regex(regex="^\\/(\\w+\\/?)+$", message="form.config.wspath.regex")
	private String wsPath;
	
	@NotNull(message="args.defect")
	@Length(max=5, min=4, message="form.config.wsserverport.length")
	@Regex(regex="^\\d+", message="form.config.wsserverport.regex")
	private String wsServerPort;
	
	@NotNull(message="args.defect")
	@Length(max=5, min=4, message="form.config.wsclientport.length")
	@Regex(regex="^\\d+", message="form.config.wsclientport.regex")
	private String wsClientPort;

	public String getStoreRoot() {
		return storeRoot;
	}

	public void setStoreRoot(String storeRoot) {
		this.storeRoot = storeRoot;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getOauthPath() {
		return oauthPath;
	}

	public void setOauthPath(String oauthPath) {
		this.oauthPath = oauthPath;
	}

	public String getWsProtocol() {
		return wsProtocol;
	}

	public void setWsProtocol(String wsProtocol) {
		this.wsProtocol = wsProtocol;
	}

	public String getWsHost() {
		return wsHost;
	}

	public void setWsHost(String wsHost) {
		this.wsHost = wsHost;
	}

	public String getWsPath() {
		return wsPath;
	}

	public void setWsPath(String wsPath) {
		this.wsPath = wsPath;
	}

	public String getWsServerPort() {
		return wsServerPort;
	}

	public void setWsServerPort(String wsServerPort) {
		this.wsServerPort = wsServerPort;
	}

	public String getWsClientPort() {
		return wsClientPort;
	}

	public void setWsClientPort(String wsClientPort) {
		this.wsClientPort = wsClientPort;
	}
	
}
