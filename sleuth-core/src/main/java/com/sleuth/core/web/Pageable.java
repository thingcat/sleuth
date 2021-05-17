package com.sleuth.core.web;

import java.io.Serializable;
import java.util.List;

/** 分页
 * 
 * @author Jonse
 * @date 2020年3月10日
 * @param <T>
 */
public class Pageable<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4639073775617201292L;

	/**
	 * 每页默认的项数(10)
	 */
	public static final int DEFAULT_ITEMS_PER_PAGE = 15;

	/**
	 * 滑动窗口默认的大小(7)
	 */
	public static final int DEFAULT_SLIDER_SIZE = 7;

	/**
	 * 表示项数未知(<code>Integer.MAX_VALUE</code>)
	 */
	public static final int UNKNOWN_ITEMS = Integer.MAX_VALUE;
	
	public static final int DEFAULT_PAGE_RANGE = 9;
	
	public static final int DEFAULT_PAGE_SPAN = 4;

	/**
	 * 状态量
	 */
	private int page; // 当前页码
	private int total; // 总共项数
	private int offset = 0;
	private List<T> datas;//数据
	private int limit; // 每页数据大小。
	
	/**
	 * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，默认每页显示<code>10</code>项
	 */
	public Pageable() {
		this(0);
	}

	/**
	 * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，指定每页项数
	 * 
	 * @param itemsPerPage
	 *            每页项数。
	 */
	public Pageable(int limit) {
		this(limit, 0);
	}

	/**
	 * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，指定每页项数
	 * 
	 * @param limit 每页项数
	 * @param total 总项数
	 */
	public Pageable(int limit, int total) {
		this.total = (total >= 0) ? total : 0;
		this.limit = (limit > 0) ? limit : DEFAULT_ITEMS_PER_PAGE;
		this.page = calcPage(0);
	}
	
	/**
	 * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，指定每页项数
	 * 
	 * @param limit 每页项数
	 * @param total 总项数
	 * @param page 页码
	 * 
	 */
	public Pageable(int limit, int total, int page) {
		this.total = (total >= 0) ? total : 0;
		this.limit = (limit > 0) ? limit : DEFAULT_ITEMS_PER_PAGE;
		this.setPage(page);
	}
	
	public void setPaging(int limit, int total) {
		this.total = (total >= 0) ? total : 0;
		this.limit = (limit > 0) ? limit : DEFAULT_ITEMS_PER_PAGE;
		this.page = calcPage(0);
	}

	/**
	 * 取得总页数。
	 * 
	 * @return 总页数
	 */
	public int getPages() {
		return (int) (total % limit == 0 ? total / limit : total / limit + 1);
	}

	/**
	 * 取得当前页。
	 * 
	 * @return 当前页
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 设置并取得当前页
	 * 
	 * @param page
	 *            当前页
	 * @return 设置后的当前页
	 */
	public int setPage(int page) {
		if (page > 0) {
			this.page = page;
		}
		return (this.page = calcPage(page));
	}

	/**
	 * 取得总项数。
	 * 
	 * @return 总项数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置并取得总项数。如果指定的总项数小于0，则被看作0
	 * 
	 * @param items总项数
	 * @return 设置以后的总项数
	 */
	public int setTotal(int total) {
		this.total = (total >= 0) ? total : 0;
		setPage(page);
		return this.total;
	}
	
	public int getOffset() {
		if (limit > 0) {
			if (page > 0) {
				offset = limit * (page - 1);
			}
		}
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * 取得每页项数。
	 * 
	 * @return 每页项数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 设置并取得每页项数。如果指定的每页项数小于等于0，则使用默认值<code>DEFAULT_ITEMS_PER_PAGE</code>
	 * 并调整当前页使之在改变每页项数前后显示相同的项
	 * 
	 * @param itemsPerPage
	 *            每页项数
	 * @return 设置后的每页项数
	 */
	public int setLimit(int limit) {
		int tmp = this.limit;
		this.limit = (limit > 0) ? limit : DEFAULT_ITEMS_PER_PAGE;
		if (page > 0) {
			setPage((int) (((double) (page - 1) * tmp) / this.limit) + 1);
		}
		return this.limit;
	}

	/**
	 * 取得当前页的长度，即当前页的实际项数。相当于 <code>endIndex() - beginIndex() + 1</code>
	 * 
	 * @return 当前页的长度
	 */
	public int getLength() {
		if (page > 0) {
			return Math.min(limit * page, total) - (limit * (page - 1));
		} else {
			return 0;
		}
	}

	/**
	 * 取得首页页码。
	 * 
	 * @return 首页页码
	 */
	public int getFirstPage() {
		return calcPage(1);
	}

	/**
	 * 取得末页页码。
	 * 
	 * @return 末页页码
	 */
	public int getLastPage() {
		return calcPage(getPages());
	}

	/**
	 * 取得前一页页码。
	 * 
	 * @return 前一页页码
	 */
	public int getPrevPage() {
		return calcPage(page - 1);
	}

	/**
	 * 取得前n页页码
	 * 
	 * @param n
	 *            前n页
	 * @return 前n页页码
	 */
	public int getPrevPage(int n) {
		int prev = calcPage(page - n);
		if (prev + DEFAULT_PAGE_RANGE > getPages()) {
			prev = getPages() - DEFAULT_PAGE_RANGE;
		}
		return prev < 1 ? 1 : prev;
	}

	/**
	 * 取得后一页页码。
	 * 
	 * @return 后一页页码
	 */
	public int getNextPage() {
		return calcPage(page + 1);
	}

	/**
	 * 取得后n页页码。
	 * 
	 * @param n
	 *            后n面
	 * @return 后n页页码
	 */
	public int getNextPage(int n) {
		int next = calcPage(page + n);
		if (next <= DEFAULT_PAGE_RANGE) {
			next = DEFAULT_PAGE_RANGE;
		}
		return next > getPages() ? getPages() : next;
	}

	/**
	 * 判断指定页码是否被禁止，也就是说指定页码超出了范围或等于当前页码。
	 * 
	 * @param page
	 *            页码
	 * @return boolean 是否为禁止的页码
	 */
	public boolean isDisabledPage(int page) {
		return ((page < 1) || (page > getPages()) || (page == this.page));
	}

	/**
	 * 计算页数，但不改变当前页。
	 * 
	 * @param page 页码
	 * @return 返回正确的页码(保证不会出边界)
	 */
	protected int calcPage(int page) {
		int pages = getPages();
		if (pages > 0) {
			return (page < 1) ? 1 : ((page > pages) ? pages : page);
		}
		return 0;
	}

	/**
	 * 创建复本。
	 * 
	 * @return 复本
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (java.lang.CloneNotSupportedException e) {
			return null; // 不可能发生
		}
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

}
