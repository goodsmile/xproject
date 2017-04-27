package com.certusnet.xproject.common.support;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * 当前页的前后margin
	 */
	private int pageMargin = 2;
	
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

	/**
	 * 分页页码列表
	 * 例如: 
	 * [1,2,3,4,5,null,10] 其中null代表省略号...
	 * 
	 * @return
	 */
	public List<Integer> getPageItems() {
		List<Integer> pageItems = new ArrayList<Integer>();
		if(pageMargin < 1){
			throw new IllegalArgumentException("'pageMargin' can not less than 1!");
		}
		if(currentPage < 1){
			throw new IllegalArgumentException("'currentPage' can not less than 1!");
		}
		if(totalPageCount < 0){
			throw new IllegalArgumentException("'totalPageCount' can not less than 0!");
		}
		if(totalPageCount == 0 || totalPageCount < currentPage){
			return pageItems;
		}
		int start = currentPage - pageMargin;
		int end = currentPage + pageMargin;
		if(start <= 0){
			int offset = Math.abs(start) + 1;
			start = start + offset;
			end = end + offset;
		}
		if(end > totalPageCount){
			int offset = totalPageCount - end;
			end = end + offset;
			start = start + offset;
			start = start < 1 ? 1 : start;
		}
		for(int i = start; i <= end; i++){
			if(i == start && i > 1){ //first
				pageItems.add(1);
				if(i - 1 > 2){
					pageItems.add(null);
				}else if(i - 1 == 2){
					pageItems.add(i - 1);
				}
			}
			pageItems.add(i);
			if(i == end && i < totalPageCount){ //last
				if(totalPageCount - end > 2){
					pageItems.add(null);
				}else if(totalPageCount - end == 2){
					pageItems.add(totalPageCount - 1);
				}
				pageItems.add(totalPageCount);
			}
		}
		return pageItems;
	}

	public int getPageMargin() {
		return pageMargin;
	}

	public void setPageMargin(int pageMargin) {
		this.pageMargin = pageMargin;
	}
	
	public String toString() {
		return "Pager [currentPage=" + currentPage + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + "]";
	}

}
