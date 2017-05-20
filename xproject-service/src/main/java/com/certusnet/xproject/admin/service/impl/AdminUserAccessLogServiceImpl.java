package com.certusnet.xproject.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.dao.AdminUserAccessLogDAO;
import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.admin.service.AdminUserAccessLogService;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;

@Service("adminUserAccessLogService")
public class AdminUserAccessLogServiceImpl implements AdminUserAccessLogService {

	@Resource(name="adminUserAccessLogDAO")
	private AdminUserAccessLogDAO adminUserAccessLogDAO;
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void recordUserAccessLog(AdminUserAccessLog accessLog) {
		adminUserAccessLogDAO.insertUserAccessLog(accessLog);
	}

	public AdminUserAccessLog getUserAccessLogById(Long id) {
		return adminUserAccessLogDAO.getUserAccessLogById(id);
	}

	public PagingList<AdminUserAccessLog> getUserAccessLogList(AdminUserAccessLog condition, Pager pager, OrderBy orderBy) {
		List<AdminUserAccessLog> dataList = adminUserAccessLogDAO.getUserAccessLogList(condition, pager, orderBy);
		return new PagingList<AdminUserAccessLog>(dataList, pager.getTotalRowCount());
	}

}
