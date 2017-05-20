package com.certusnet.xproject.admin.service;

import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;

/**
 * 用户操作日志Service
 * 
 * @author 	pengpeng
 * @date   		2017年5月13日 上午11:29:18
 * @version 	1.0
 */
public interface AdminUserAccessLogService {

	/**
	 * 记录用户操作日志
	 * @param accessLog
	 */
	public void recordUserAccessLog(AdminUserAccessLog accessLog);
	
	/**
	 * 根据日志ID获取日志详情
	 * @param id
	 * @return
	 */
	public AdminUserAccessLog getUserAccessLogById(Long id);
	
	/**
	 * 根据条件查询日志列表
	 * @param condition
	 * @param pager
	 * @param orderBy
	 * @return
	 */
	public PagingList<AdminUserAccessLog> getUserAccessLogList(AdminUserAccessLog condition, Pager pager, OrderBy orderBy);
	
}
