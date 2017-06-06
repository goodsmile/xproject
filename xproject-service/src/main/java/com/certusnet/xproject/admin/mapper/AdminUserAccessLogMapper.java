package com.certusnet.xproject.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.common.support.DefaultDatabase;
import com.certusnet.xproject.common.support.OrderBy;

/**
 * 用户操作日志DAO
 * 
 * @author 	pengpeng
 * @date   		2017年5月13日 上午11:29:18
 * @version 	1.0
 */
@DefaultDatabase
public interface AdminUserAccessLogMapper {

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
	public AdminUserAccessLog selectUserAccessLogById(Long id);
	
	/**
	 * 根据条件查询日志列表
     * @param condition
     * @param orderBy
     * @param rowBounds
	 * @return
	 */
	public List<AdminUserAccessLog> selectUserAccessLogList(@Param("condition")AdminUserAccessLog condition, @Param("orderBy")OrderBy orderBy, RowBounds rowBounds);
	
	public int countUserAccessLogList(@Param("condition")AdminUserAccessLog condition);
	
}
