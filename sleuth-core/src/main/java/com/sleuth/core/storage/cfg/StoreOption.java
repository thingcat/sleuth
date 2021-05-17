package com.sleuth.core.storage.cfg;

import java.util.Arrays;
import java.util.List;

import org.rocksdb.Statistics;

/** 数据库属性配置
 * 
 * @author Jonse
 * @date 2020年1月13日
 */
public class StoreOption {
	
	//库的数量
	private List<Integer> dbs = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	//根路径
	private String root;
	//默认选择的库
	private Integer db;
	//是否自动创建目录
	private boolean createIfMissing;
	//刷盘的数据大小，默认值：4 * 1024kb
	private Integer writeBufferSize;
	//在内存中建立的写入缓冲区的最大数目，默认值：2
	private Integer maxWriteBufferNumber;
	//指定提交到默认低优先级线程池的并发后台压缩作业的最大数量，默认值：1
	private Integer maxBackgroundCompactions;
	//用于分析数据库性能的统计信息
	private Statistics statistics;
		
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public boolean getCreateIfMissing() {
		return createIfMissing;
	}
	public void setCreateIfMissing(boolean createIfMissing) {
		this.createIfMissing = createIfMissing;
	}
	public Statistics getStatistics() {
		return statistics;
	}
	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	public Integer getWriteBufferSize() {
		return writeBufferSize;
	}
	public void setWriteBufferSize(Integer writeBufferSize) {
		this.writeBufferSize = writeBufferSize;
	}
	public Integer getMaxWriteBufferNumber() {
		return maxWriteBufferNumber;
	}
	public void setMaxWriteBufferNumber(Integer maxWriteBufferNumber) {
		this.maxWriteBufferNumber = maxWriteBufferNumber;
	}
	public Integer getMaxBackgroundCompactions() {
		return maxBackgroundCompactions;
	}
	public void setMaxBackgroundCompactions(Integer maxBackgroundCompactions) {
		this.maxBackgroundCompactions = maxBackgroundCompactions;
	}
	public Integer getDb() {
		return db;
	}
	public void setDb(Integer db) {
		this.db = db;
	}
	public List<Integer> getDbs() {
		return dbs;
	}
	public void setDbs(List<Integer> dbs) {
		this.dbs = dbs;
	}
}
