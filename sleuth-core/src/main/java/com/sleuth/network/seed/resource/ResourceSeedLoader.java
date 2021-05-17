package com.sleuth.network.seed.resource;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.seed.SeedLoader;
import com.sleuth.network.service.NodeInfoService;

/** 种子资源文件加载
 * 
 * @author Jonse
 * @date 2020年9月26日
 */
@Component
public class ResourceSeedLoader implements SeedLoader {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${seed.path:/}")
	private String path;
	
	private static final String SEED_FILE_NAME = "seed.json";
	
	@Resource
	private NodeInfoService nodeInfoService;
	
	@Override
	public void unpack() {
		//种子文件是一个JSON格式的文件
		String jsonText = this.load();
		if (jsonText != null) {
			try {
				JSONArray jsonArray = JSONArray.parseArray(jsonText);
				if (jsonArray != null && jsonArray.size() > 0) {
					for(int i=0,size=jsonArray.size(); i<size; i++) {
						NodeInfo nodeInfo = jsonArray.getObject(i, NodeInfo.class);
						this.nodeInfoService.add(nodeInfo);
						logger.debug("import uri = {}", nodeInfo.getUri());
					}
					logger.info("{} seed nodes were imported", jsonArray.size());
				}
			} catch (Exception e) {
				logger.error("The JSON format of the seed file could not be resolved.");
			}
		}
	}
	
	@Override
	public String pack(List<NodeInfo> nodeInfos) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String load() {
		try {
			URL url = this.getClass().getResource(this.path);
			URI uri = new URI(url.toString() + SEED_FILE_NAME);
			Path path = Paths.get(uri);
			logger.debug("seed file at = {}", path.toString());
			byte[] bytes = Files.readAllBytes(path);
			return new String(bytes);
		} catch (Exception e) {
			logger.error("Loader {} error.", SEED_FILE_NAME, e);
		}
		return null;
	}

}
