package com.certusnet.xproject.common.support;

/**
 * 通用分页Pager对象
 * 
 * @author pengpeng
 * @date 2013-10-14 下午10:30:15
 * @version 1.0
 */
public class Pager {

	/**
	 * 当前页码
	 */
	private int currentPage = 1;
	
	/**
	 * 每页显示多少条
	 */
	private int pageSize = 10;
	
	/**
	 * 查询总记录数
	 */
	private int totalRowCount = 0;
	
	/**
	 * 可分多少页
	 */
	private int totalPageCount = 0;
	
	public Pager() {
		super();
	}

	public Pager(Integer currentPage, Integer pageSize) {
		super();
		if(currentPage != null && currentPage > 0){
			this.currentPage = currentPage;
		}
		if(pageSize != null && pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	public Pager(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		this(currentPage, pageSize);
		if(totalRowCount != null){
			this.totalRowCount = totalRowCount;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
		getTotalPageCount(); //计算totalPageCount
	}

	public int getTotalPageCount() {
		if(totalRowCount <= 0){
			totalPageCount = 0;
		}else{
			totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
		}
		return totalPageCount;
	}

	public int getOffset() {
		return (currentPage - 1) * pageSize;
	}
	
	public int getLimit() {
		return pageSize;
	}
	
	public String toString() {
		return "Pager [currentPage=" + currentPage + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + "]";
	}

}
