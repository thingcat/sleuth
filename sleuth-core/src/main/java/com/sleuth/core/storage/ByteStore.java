package com.sleuth.core.storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** 基于byte的存储模型
 * 
 * @author Jonse
 * @param <T>
 * @date 2020年1月16日
 */
public interface ByteStore<K, V> {
	
	/** 根据key获得数据
	 * 
	 * @param key
	 * @return
	 */
	public abstract byte[] get(String familyName, byte[] key);
	
	/** 设置信息
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void set(String familyName, byte[] key, byte[] value);
	
	/** 是否存在相同个的key
	 * 
	 * @param key
	 * @return
	 */
	public abstract boolean contains(String familyName, byte[] key);
	
	/** 从库里面删除
	 * 
	 * @param key
	 */
	public abstract void delete(String familyName, byte[] key);
	
	/** 通过List做主键查询
	 * 
	 * @param keys
	 * @return
	 */
	public abstract List<V> get(String familyName, List<K> keys);
	
	public abstract Iterable<Map.Entry<K, V>> forEach();

	public abstract interface Validator<V> {
		
		public abstract boolean isValid(V value);
		
	}

	public static class FindByRange<K> {
		
		private K start;
		private boolean startInclusive;
		private K end;
		private boolean endInclusive;

		public FindByRange() {
			super();
		}

		public FindByRange(K start, boolean startInclusive, K end, boolean endInclusive) {
			this.start = start;
			this.startInclusive = startInclusive;
			this.end = end;
			this.endInclusive = endInclusive;
		}

		public FindByRange<K> setStart(K start, boolean inclusive) {
			this.start = start;
			this.startInclusive = inclusive;
			return this;
		}

		public FindByRange<K> setEnd(K end, boolean inclusive) {
			this.end = end;
			this.endInclusive = inclusive;
			return this;
		}

		public K getStart() {
			return start;
		}

		public boolean isStartInclusive() {
			return startInclusive;
		}

		public K getEnd() {
			return end;
		}

		public boolean isEndInclusive() {
			return endInclusive;
		}

		@Override
		public int hashCode() {
			return Objects.hash(start, end, startInclusive, endInclusive);
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof FindByRange)) {
				return false;
			}
			FindByRange<?> that = (FindByRange<?>) obj;
			return this.startInclusive == that.startInclusive && this.endInclusive == that.endInclusive
					&& Objects.equals(this.start, that.start) && Objects.equals(this.end, that.end);
		}

	}
	
}
