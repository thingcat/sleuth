package com.sleuth.core.script.actuator;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;

/** 参数
 * 
 * @author Jonse
 * @date 2020年9月16日
 */
public class AxParams {
	
	private Entry[] entries = {};//外部参数
	private String[] axIds = {};//引用的其他交易输出
	private int j = -1;//迭代索引
	
	public void set(String name, Object value) {
		Entry axEntry = new Entry(name, value);
		//检查是否存在
		int idx = ArrayUtils.indexOf(this.entries, axEntry);
		if (idx > -1) {
			//存在，则替换掉原有的
			this.entries[idx] = axEntry;
		} else {
			//不存在，则直接存入
			this.entries = ArrayUtils.add(this.entries, axEntry);
		}
	}
	
	public Object get(String name) {
		//检查是否存在
		int idx = ArrayUtils.indexOf(this.entries, name);
		if (idx > -1) {
			return this.entries[idx].getValue();
		}
		return null;
	}
	
	public void remove(String name) {
		//检查是否存在
		int idx = ArrayUtils.indexOf(this.entries, name);
		if (idx > -1) {
			//存在则删除
			this.entries = ArrayUtils.remove(this.entries, idx);
		}
	}
	
	/** 引用其他交易
	 * 
	 * @param axId
	 */
	public void addAxId(String axId) {
		this.axIds = ArrayUtils.add(this.axIds, axId);
	}
	
	public String[] getAxIds() {
		return this.axIds;
	}
	
	public Entry[] values() {
		return this.entries;
	}
	
	public boolean hasNext() {
		int size = this.size();
		if (size > 0) {
			if (this.j == -1) {
				this.j = 0;
			}
			if (this.j < size) {
				return true;
			}
			//迭代完成，索引重定向到-1
			this.j = -1;
		}
		return false;
	}
	
	public Entry next() {
		Entry entry = this.entries[this.j];
		this.j++;
		return entry;
	}
	
	public int size() {
		return this.entries.length;
	}
	
	public String toJSON() {
		return JSON.toJSONString(this);
	}
	
}
