package com.certusnet.xproject.admin.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.mapper.AdminUserAccessLogMapper;
import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.admin.service.AdminUserAccessLogService;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;

@Service("adminUserAccessLogService")
public class AdminUserAccessLogServiceImpl implements AdminUserAccessLogService {

	@Autowired
	private AdminUserAccessLogMapper adminUserAccessLogMapper;
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void recordUserAccessLog(AdminUserAccessLog accessLog) {
		adminUserAccessLogMapper.insertUserAccessLog(accessLog);
	}

	public AdminUserAccessLog getUserAccessLogById(Long id) {
		return adminUserAccessLogMapper.selectUserAccessLogById(id);
	}

	public PagingList<AdminUserAccessLog> getUserAccessLogList(AdminUserAccessLog condition, Pager pager, OrderBy orderBy) {
		List<AdminUserAccessLog> dataList = adminUserAccessLogMapper.selectUserAccessLogList(condition, orderBy, new RowBounds(pager.getOffset(), pager.getLimit()));
		return new PagingList<AdminUserAccessLog>(dataList, adminUserAccessLogMapper.countUserAccessLogList(condition));
	}

}
