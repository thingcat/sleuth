package com.sleuth.block.mine.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.mine.SleuthAddresser;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.weburi.WebUriBuffer;
import com.sleuth.block.weburi.WebUriPool;
import com.sleuth.core.utils.PublicKeyUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/** 寻址器
 * 
 * @author Jonse
 * @date 2021年5月12日
 */
public class CrawlerSleuthAddresser implements SleuthAddresser {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final CrawlerConfig config;
	private final CrawlController crawlController;
	
	private final WebUriBuffer webUriBuffer;
	private final WebUriPool webUriPool;
	private final byte[] pubKeyHash;
	
	public CrawlerSleuthAddresser(WebUriBuffer webUriBuffer, WebUriPool webUriPool, String address) {
		this.webUriBuffer = webUriBuffer;
		this.webUriPool = webUriPool;
		this.pubKeyHash = PublicKeyUtils.addressToPubKeyHash(address);
				
		this.config = new CrawlerConfig();
		
		//获取配置信息
		CrawlConfig crawlConfig = this.config.getCrawlConfig();
		// 实例化页面获取器
		PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		// 实例化爬虫机器人配置
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		// 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
		// 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		try {
			// 实例化爬虫控制器
			this.crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			logger.error("failed to initialize the addresser. ", e);
			throw new SleuthAddresserException();
		}
	}
	
	@Override
	public void start() {
		WebUri webUri = this.webUriPool.random();
		this.start(webUri.getUri());
	}
	
	@Override
	public void start(final String... seeds) {
		for (String seed : seeds) {
			this.crawlController.addSeed(seed);
			logger.debug("add seed {}", seed);
		}
		AddresserRunner addresserRunner = new AddresserRunner(this.webUriBuffer, this.webUriPool, this.pubKeyHash);
		new Thread(addresserRunner).start();
		logger.info("The addresser was started successfully.");
	}
	
	@Override
	public void shutdown() {
		this.crawlController.shutdown();
		this.start();
	}
	
	/** 寻址器工作线程
	 * 
	 * @author Jonse
	 * @date 2021年5月16日
	 */
	protected class AddresserRunner implements Runnable {
		
		private WebUriBuffer webUriBuffer;
		private WebUriPool webUriPool;
		private byte[] pubKeyHash;
		
		public AddresserRunner(WebUriBuffer webUriBuffer, WebUriPool webUriPool, byte[] pubKeyHash) {
			this.webUriBuffer = webUriBuffer;
			this.webUriPool = webUriPool;
			this.pubKeyHash = pubKeyHash;
		}
		
		public void run() {
			Thread.currentThread().setName("addresser");
			CrawlerAddresser crawlerAddresser = new CrawlerAddresser(this.webUriBuffer, this.webUriPool, this.pubKeyHash);
			CrawlController.WebCrawlerFactory<CrawlerAddresser> crawlerFactory = ()-> crawlerAddresser;
			crawlController.start(crawlerFactory, config.getNumberOfCrawlers());
			logger.info("complete the addressing work.");
			shutdown();
		}
		
	}
	
}
