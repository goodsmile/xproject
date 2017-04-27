package com.certusnet.xproject.common.support;

import java.io.Serializable;
import java.util.List;

public class PagingList<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private Integer currentPage;
	
	/** 每页显示多少页 */
	private Integer pageSize;
	
	/** 共多少页 */
	private Integer totalPageCount;
	
    /** 当存在分页查询时此值为总记录数 */
	private Integer totalRowCount;
	
	/** 数据结果集 */
	private List<T> dataList;

	public PagingList() {
		super();
	}
	
	public PagingList(List<T> dataList, Pager pager) {
		super();
		this.dataList = dataList;
		setPager(pager);
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	
	public void setPager(Pager pager) {
		this.currentPage = pager.getCurrentPage();
		this.pageSize = pager.getPageSize();
		this.totalRowCount = pager.getTotalRowCount();
		this.totalPageCount = pager.getTotalPageCount();
	}

	public String toString() {
		return "PagingList [currentPage=" + currentPage + ", pageSize="
				+ pageSize + ", totalPageCount=" + totalPageCount
				+ ", totalRowCount=" + totalRowCount + ", dataList=" + dataList
				+ "]";
	}
	
}
