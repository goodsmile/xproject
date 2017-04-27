package com.certusnet.xproject.common.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 日志处理类,如将日志写入数据库、日志文件等等
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:08
 * @version  	1.0
 */
public abstract class AbstractHttpRequestLoggerHandler<T> implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpRequestLoggerHandler.class);
	
	private final HttpRequestLogger<T> httpRequestLogger;
	
	public AbstractHttpRequestLoggerHandler(HttpRequestLogger<T> httpRequestLogger) {
		super();
		this.httpRequestLogger = httpRequestLogger;
	}

	public void run() {
		try {
			handleLogger(httpRequestLogger);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 日志处理方法,如将日志写入数据库、日志文件等等
	 * @param httpRequestLogger
	 * @throws Exception
	 */
	public abstract void handleLogger(HttpRequestLogger<T> httpRequestLogger) throws Exception;

}
