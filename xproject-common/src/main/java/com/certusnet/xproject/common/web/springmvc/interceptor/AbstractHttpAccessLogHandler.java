package com.certusnet.xproject.common.web.springmvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.certusnet.xproject.common.support.HttpAccessLogging;
/**
 * 日志处理类,如将日志写入数据库、日志文件等等
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:08
 * @version  	1.0
 */
public abstract class AbstractHttpAccessLogHandler<T> implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpAccessLogHandler.class);
	
	private final HttpAccessLog<T> httpAccessLog;
	
	public AbstractHttpAccessLogHandler(HttpAccessLog<T> httpAccessLog) {
		super();
		this.httpAccessLog = httpAccessLog;
	}

	public void run() {
		try {
			handleLogger(httpAccessLog);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取日志的记录方式
	 * @return
	 */
	public abstract HttpAccessLogging.LoggingType getLoggingType();
	
	/**
	 * 日志处理方法,如将日志写入数据库、日志文件等等
	 * @param httpAccessLog
	 * @throws Exception
	 */
	public abstract void handleLogger(HttpAccessLog<T> httpAccessLog) throws Exception;

}
