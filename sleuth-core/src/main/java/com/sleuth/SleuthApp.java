package com.sleuth;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.config.Config;
import com.sleuth.core.config.DefaultConfig;

public class SleuthApp {
	
	static final Class<SleuthApp> classLoader = SleuthApp.class;
	static final Logger logger = LoggerFactory.getLogger(SleuthApp.class);
	
	static final String app_name = "app.name";
	static final String app_context = "app.ctxpath";
	static final String app_port = "app.port";
	
	static String appName = null;
	static String appContext = null;
	static String appPort = null;
	
	static {
		final Config config = DefaultConfig.getInstance();
		appName = config.get(app_name);
		appContext = config.get(app_context);
		appPort = config.get(app_port);
	}
	
	static void start() throws Exception {
		
		String webXmlPath = classLoader.getResource("/web.xml").toString();
		Path webXml = Paths.get(new URI(webXmlPath));
		String webRoot = System.getProperty("user.dir");
		
		Server server = new Server(Integer.valueOf(appPort));
		// 设置在JVM退出时关闭Jetty的钩子。
        server.setStopAtShutdown(true);
        
		WebAppContext context = new WebAppContext();
		context.setContextPath(appContext);
		context.setDisplayName(appName);
		context.setDescriptor(webXml.toString());
		context.setResourceBase(webRoot);
		context.setParentLoaderPriority(true);
		
		server.setHandler(context);
		
		server.start();
		server.join();
		
	}

	public static void main(String[] args) throws Exception {
		
		SleuthApp.start();
		
		System.out.println("=====>>>");
		
	}

}
