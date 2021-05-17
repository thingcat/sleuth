package com.sleuth.api.dto;

import java.io.Serializable;

public class ConfigDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3978597785940551042L;

	private String storeRoot;
	private String db;
	private String oauthPath;
	
	private String wsProtocol;
	private String wsHost;
	private String wsPath;
	private String wsServerPort;
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
