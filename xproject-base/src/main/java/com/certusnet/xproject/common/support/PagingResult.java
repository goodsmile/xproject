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

    /** 当存在分页查询时此值为总记录数 */
	private int totalRowCount = 0;

	public PagingResult() {
		super();
	}

	public PagingResult(boolean success, String code, String message, T data, int totalRowCount) {
		super(success, code, message, data);
		this.totalRowCount = totalRowCount;
	}
	
	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
}
