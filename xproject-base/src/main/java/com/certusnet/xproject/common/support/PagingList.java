package com.certusnet.xproject.common.support;

import java.io.Serializable;
import java.util.List;

public class PagingList<T> implements Serializable {

	private static final long serialVersionUID = 1L;

    /** 当存在分页查询时此值为总记录数 */
	private int totalRowCount = 0;
	
	/** 数据结果集 */
	private List<T> dataList;

	public PagingList() {
		super();
	}
	
	public PagingList(List<T> dataList, int totalRowCount) {
		super();
		this.dataList = dataList;
		this.totalRowCount = totalRowCount;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public String toString() {
		return "PagingList [totalRowCount=" + totalRowCount + ", dataList=" + dataList + "]";
	}
	
}
