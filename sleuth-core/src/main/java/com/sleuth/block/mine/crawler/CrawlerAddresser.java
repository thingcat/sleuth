package com.sleuth.block.mine.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.weburi.WebUriBuffer;
import com.sleuth.block.weburi.WebUriPool;
import com.sleuth.core.utils.DateUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/** WEB寻址器
 * 
 * @author Jonse
 * @date 2021年5月12日
 */
public class CrawlerAddresser extends WebCrawler {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final Map<String, WebUri> WebURIs = Maps.newConcurrentMap();
	
//	@Override
//    public boolean shouldVisit(Page referringPage, WebURL url) {
//        String href = url.getURL().toLowerCase();
//        return FILTERS.matcher(href).matches();
//		return false;
//    }
	
	private WebUriBuffer webUriBuffer;
	private WebUriPool webUriPool;
	private byte[] pubKeyHash;
	
	public CrawlerAddresser(WebUriBuffer webUriBuffer, WebUriPool webUriPool, byte[] pubKeyHash) {
		this.webUriPool = webUriPool;
		this.webUriBuffer = webUriBuffer;
		this.pubKeyHash = pubKeyHash;
	}
	
	@Override
	public void visit(Page webPage) {
		WebURL webURL = webPage.getWebURL();
		try {
			URL url = new URL(webURL.getURL());
			String protocol = url.getProtocol();
			String domain = webURL.getDomain();
	        String uri = protocol + "://www." + domain + "/";
	        
	        WebUri webUri = new WebUri();
	        webUri.setDomain(domain);
	        webUri.setUri(uri);
	        webUri.setPubKeyHash(this.pubKeyHash);
	        webUri.setCreateAt(DateUtils.nowToUtc());
	        
	        ParseData parseData = webPage.getParseData();
	        if (parseData instanceof HtmlParseData) {
	        	HtmlParseData htmlParse = (HtmlParseData) parseData;
	        	String title = htmlParse.getTitle();
	        	webUri.setTitle(title);
	        }
	        String wxId = webUri.toWxId();
	        webUri.setWxId(webUri.toWxId());
	        
	        WebUri poolWebUri = webUriPool.get(wxId);
	        if (poolWebUri == null) {
				//加入缓存中
	        	this.webUriBuffer.push(poolWebUri);
	        	logger.debug("Find the address {}, join the buffer pool and wait to be packaged", domain);
			}
	        
		} catch (MalformedURLException e) {
			logger.error("Error parsing URI: {}", webURL.getPath());
		}
		
	}
	
}
