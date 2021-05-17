package com.sleuth.block.mine.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.config.Config;
import com.sleuth.core.config.DefaultConfig;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/** 爬虫配置
 * 
 * @author Jonse
 * @date 2018年11月14日
 */
public class CrawlerConfig {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
    
	public static final String KEY_STORAGE_FOLDER = "crawler.store.folder";
	public static final String VALUE_STORAGE_FOLDER = "/crawler";
	
	public static final String KEY_DEPTHOF_CRAWLING = "crawler.depthof.crawling";
	public static final String VALUE_DEPTHOF_CRAWLING = "3";//爬虫深度
	
	public static final String KEY_NUMBER_OF_CRAWLERS = "crawler.number.crawlers";
	public static final String VALUE_NUMBER_OF_CRAWLERS = "4";//线程数
	
	public static final String KEY_INCLUDE_HTTPS_PAGES = "crawler.https.pages";
	public static final String VALUE_INCLUDE_HTTPS_PAGES = "true";//简单启用SSL
	
	public static final String KEY_BINARY_CONTENT_IN_CRAWLING = "crawler.binary.crawling";
	public static final String VALUE_BINARY_CONTENT_IN_CRAWLING = "false";//二进制抓取
	
	public static final String KEY_POLITENESS_DELAY = "crawler.politeness.delay";
	public static final String VALUE_POLITENESS_DELAY = "200";//请求之间等待至少200毫秒
	
	public static final String KEY_RESUMABLE_CRAWLING = "crawler.resumable.crawling";
	public static final String VALUE_RESUMABLE_CRAWLING = "false";//搜索器可能意外终止恢复工作
	
	
	private String storageFolder;//文件存储位置
	private int numberOfCrawlers;//线程数
	private int maxDepthOfCrawling;//深度
	private boolean includeHttpsPages;//简单启用SSL
	private boolean binaryContentInCrawling;//二进制抓取
	private int politenessDelay;//请求之间等待至少200毫秒
	private boolean resumableCrawling;//搜索器可能意外终止恢复工作
	
    private final CrawlConfig crawlConfig = new CrawlConfig();
	
	public CrawlerConfig() {
		
		Config config = DefaultConfig.getInstance();
		
		this.numberOfCrawlers = Integer.valueOf(config.get(KEY_NUMBER_OF_CRAWLERS, VALUE_NUMBER_OF_CRAWLERS));
		
    	this.storageFolder = config.get(KEY_STORAGE_FOLDER, VALUE_STORAGE_FOLDER);
    	this.crawlConfig.setCrawlStorageFolder(this.storageFolder);
    	
    	this.maxDepthOfCrawling = Integer.valueOf(config.get(KEY_DEPTHOF_CRAWLING, VALUE_DEPTHOF_CRAWLING));
    	this.crawlConfig.setMaxDepthOfCrawling(this.maxDepthOfCrawling);
    	
    	this.includeHttpsPages = Boolean.valueOf(config.get(KEY_INCLUDE_HTTPS_PAGES, VALUE_INCLUDE_HTTPS_PAGES));
    	this.crawlConfig.setIncludeHttpsPages(this.includeHttpsPages);
    	
    	this.binaryContentInCrawling = Boolean.valueOf(config.get(KEY_BINARY_CONTENT_IN_CRAWLING, VALUE_BINARY_CONTENT_IN_CRAWLING));
    	this.crawlConfig.setIncludeBinaryContentInCrawling(this.binaryContentInCrawling);
    	
    	this.politenessDelay = Integer.valueOf(config.get(KEY_POLITENESS_DELAY, VALUE_POLITENESS_DELAY));
    	this.crawlConfig.setPolitenessDelay(this.politenessDelay);
    	
    	this.resumableCrawling = Boolean.valueOf(config.get(KEY_RESUMABLE_CRAWLING, VALUE_RESUMABLE_CRAWLING));
    	this.crawlConfig.setResumableCrawling(this.resumableCrawling);
    	
	}
	
	/**启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
	 * 
	 * @param crawlerClass
	 * @param seeds
	 * @throws Exception 
	 */
	public <T extends WebCrawler> void startup(Class<T> crawlerClass, String seed) throws Exception {
		PageFetcher pageFetcher = new PageFetcher(this.crawlConfig);// 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
        // 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(this.crawlConfig, pageFetcher, robotstxtServer);
        logger.debug("Added seed = {}", seed);
    	controller.addSeed(seed);
        logger.debug("thread total = {}", this.numberOfCrawlers);
        controller.start(crawlerClass, this.numberOfCrawlers);
	}
	
	public String getStorageFolder() {
		return storageFolder;
	}

	public void setStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;
	}

	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public int getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public boolean isIncludeHttpsPages() {
		return includeHttpsPages;
	}

	public void setIncludeHttpsPages(boolean includeHttpsPages) {
		this.includeHttpsPages = includeHttpsPages;
	}

	public boolean isBinaryContentInCrawling() {
		return binaryContentInCrawling;
	}

	public void setBinaryContentInCrawling(boolean binaryContentInCrawling) {
		this.binaryContentInCrawling = binaryContentInCrawling;
	}

	public int getPolitenessDelay() {
		return politenessDelay;
	}

	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}

	public boolean isResumableCrawling() {
		return resumableCrawling;
	}

	public void setResumableCrawling(boolean resumableCrawling) {
		this.resumableCrawling = resumableCrawling;
	}

	public CrawlConfig getCrawlConfig() {
		return crawlConfig;
	}
    
}
