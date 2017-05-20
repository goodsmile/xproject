package com.certusnet.xproject.admin.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.model.AdminUserAccessLog;
import com.certusnet.xproject.common.support.HttpAccessLogging.LoggingType;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.JsonUtils;
import com.certusnet.xproject.common.web.springmvc.interceptor.AbstractHttpAccessLogHandler;
import com.certusnet.xproject.common.web.springmvc.interceptor.HttpAccessLog;

public class FileStoreHttpAccessLogHandler extends AbstractHttpAccessLogHandler<AdminUser> {

	private static final Logger logger = LoggerFactory.getLogger(FileStoreHttpAccessLogHandler.class);
	
	public FileStoreHttpAccessLogHandler(HttpAccessLog<AdminUser> httpAccessLog) {
		super(httpAccessLog);
	}

	public LoggingType getLoggingType() {
		return LoggingType.DB;
	}

	public void handleLogger(HttpAccessLog<AdminUser> httpAccessLog) throws Exception {
		AdminUserAccessLog accessLog = new AdminUserAccessLog();
		accessLog.setTitle(httpAccessLog.getTitle());
		accessLog.setUri(httpAccessLog.getUri());
		accessLog.setMethod(httpAccessLog.getMethod());
		accessLog.setRequestHeader(CollectionUtils.isEmpty(httpAccessLog.getRequestHeader()) ? null : JsonUtils.object2Json(httpAccessLog.getRequestHeader()));
		accessLog.setRequestParameter(JsonUtils.object2Json(httpAccessLog.getRequestParameter()));
		accessLog.setAccessUserId(httpAccessLog.getAccessUser().getUserId());
		accessLog.setAccessTime(httpAccessLog.getAccessTime());
		accessLog.setClientIpAddr(httpAccessLog.getClientIpAddr());
		accessLog.setServerIpAddr(httpAccessLog.getServerIpAddr());
		accessLog.setProcessTime1(httpAccessLog.getProcessTime1());
		accessLog.setProcessTime2(httpAccessLog.getProcessTime2());
		accessLog.setLoggingCompleted(httpAccessLog.isLoggingCompleted());
		accessLog.setAsynRequest(httpAccessLog.isAsynRequest());
		accessLog.setCreateTime(DateTimeUtils.formatNow());
		logger.info("【用户操作日志】>>> " + accessLog);
	}

}
