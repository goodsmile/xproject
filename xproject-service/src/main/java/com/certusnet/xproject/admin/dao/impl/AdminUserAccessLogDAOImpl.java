package com.certusnet.xproject.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.certusnet.xproject.admin.dao.AdminUserAccessLogDAO;
import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.common.mybatis.DefaultBaseMybatisDAO;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;

@Repository("adminUserAccessLogDAO")
public class AdminUserAccessLogDAOImpl extends DefaultBaseMybatisDAO implements AdminUserAccessLogDAO {

	public void insertUserAccessLog(AdminUserAccessLog accessLog) {
		this.getSqlSessionTemplate().insert(getMapperKey("insertUserAccessLog"), accessLog);
	}

	public AdminUserAccessLog getUserAccessLogById(Long id) {
		return this.getSqlSessionTemplate().selectOne(getMapperKey("getUserAccessLogById"), id);
	}

	public List<AdminUserAccessLog> getUserAccessLogList(AdminUserAccessLog condition, Pager pager, OrderBy orderBy) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("condition", condition);
        paramMap.put("orderBy", orderBy);
		return this.getSqlSessionTemplate().selectList(getMapperKey("getUserAccessLogList"), paramMap, pager);
	}

	protected Class<?> getBoundModelClass() {
		return AdminUserAccessLog.class;
	}

}
