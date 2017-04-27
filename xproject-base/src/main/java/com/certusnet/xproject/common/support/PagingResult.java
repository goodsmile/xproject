package com.certusnet.xproject.common.support;
/**
 * 通用返回结果类(针对分页)
 * 
 * @param <T>
 * @author  pengpeng
 * @date 	 2015年5月7日 上午9:53:27
 * @version 1.0
 */
public class PagingResult<T> extends Result<T> {

	/** 当前页码 */
	private Integer currentPage;
	
	/** 每页显示多少页 */
	private Integer pageSize;
	
	/** 共多少页 */
	private Integer totalPageCount;
	
    /** 当存在分页查询时此值为总记录数 */
	private Integer totalRowCount;

	public PagingResult() {
		super();
	}

	public PagingResult(boolean success, String code, String message, T data) {
		super(success, code, message, data);
	}

	public PagingResult(boolean success, String code, String message) {
		super(success, code, message);
	}

	public PagingResult(boolean success, String message) {
		super(success, message);
	}

	public PagingResult(boolean success, T data) {
		super(success, data);
	}

	public PagingResult(boolean success, T data, Pager pager) {
		super(success, data);
		setPager(pager);
	}
	
	public PagingResult(boolean success) {
		super(success);
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
	
	public void setPager(Pager pager) {
		this.currentPage = pager.getCurrentPage();
		this.pageSize = pager.getPageSize();
		this.totalRowCount = pager.getTotalRowCount();
		this.totalPageCount = pager.getTotalPageCount();
	}
	
}
