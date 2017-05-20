package com.certusnet.xproject.admin.dao;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;

/**
 * 用户操作日志DAO
 * 
 * @author 	pengpeng
 * @date   		2017年5月13日 上午11:29:18
 * @version 	1.0
 */
public interface AdminUserAccessLogDAO {

	/**
	 * 插入操作日志
	 * @param accessLog
	 */
	public void insertUserAccessLog(AdminUserAccessLog accessLog);
	
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
	public List<AdminUserAccessLog> getUserAccessLogList(AdminUserAccessLog condition, Pager pager, OrderBy orderBy);
	
}
