package com.sleuth.api.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.sleuth.api.ServerApi;

@Service
public class ServerApiImpl implements ServerApi {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ConfigurableWebApplicationContext configurableWebApplicationContext;
	
	@Override
	public void start(HttpServletRequest request) {
        XmlWebApplicationContext context = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        context.start();
        configurableWebApplicationContext.refresh();
	}

	@Override
	public void reload(HttpServletRequest request) {
        XmlWebApplicationContext context = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        //重新load所有bean
        context.refresh();
        configurableWebApplicationContext.refresh();
	}

	@Override
	public void stop(HttpServletRequest request) {
		XmlWebApplicationContext context = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        context.stop();
        configurableWebApplicationContext.refresh();
	}

}
