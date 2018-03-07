package com.emobile.jets.mayapada.smi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSmiReceiverHandler {
	private static final Logger LOG_TRX = LoggerFactory.getLogger("smiGprs.transaction");
	
	private String id;
	
	protected String key;
	
	protected String serverSmsUrl;

	protected abstract Logger getLogger();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	public String getServerSmsUrl() {
		return serverSmsUrl;
	}
	public void setServerSmsUrl(String serverSmsUrl) {
		this.serverSmsUrl = serverSmsUrl;
	}

}
