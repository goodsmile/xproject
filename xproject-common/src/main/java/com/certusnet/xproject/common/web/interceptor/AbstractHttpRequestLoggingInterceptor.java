package com.certusnet.xproject.common.web.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.certusnet.xproject.common.support.HttpRequestLogging;
import com.certusnet.xproject.common.support.NamedThreadFactory;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.HttpUtils;
import com.certusnet.xproject.common.util.NetUtils;
import com.certusnet.xproject.common.util.StringUtils;

/**
 * Http请求日志记录拦截器
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午9:21:35
 * @version  	1.0
 */
public abstract class AbstractHttpRequestLoggingInterceptor implements HandlerInterceptor, DisposableBean {

	private static final Pattern messageSourceCodePattern = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)\\}");
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpRequestLoggingInterceptor.class);
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private static final ExecutorService httpRequestLoggingExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("HTTP-REQUEST-LOGGING-EXECUTE-WORKER-"));
	
	private static final ThreadLocal<HttpRequestLogger<Object>> httpRequestLoggerThreadLocal = new NamedThreadLocal<HttpRequestLogger<Object>>("httpRequestLogger ThreadLocal");
	
	/**
	 * 请求进入处理器方法之前调用
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			String currentRequestURI = urlPathHelper.getRequestUri(request);
			Map<String,String[]> originalParamMap = request.getParameterMap();
			Map<String,String> paramMap = new HashMap<String,String>();
			if(originalParamMap != null && !originalParamMap.isEmpty()){
				for(Map.Entry<String, String[]> entry : originalParamMap.entrySet()){
					paramMap.put(entry.getKey(), getStringParameterValue(entry.getValue()));
				}
			}
			HttpRequestLogger<Object> httpRequestLogger = new HttpRequestLogger<Object>();
			httpRequestLogger.setOptBeginMillis(System.currentTimeMillis());
			httpRequestLogger.setUri(currentRequestURI);
			httpRequestLogger.setOptTime(DateTimeUtils.formatNow());
			httpRequestLogger.setMethod(request.getMethod());
			httpRequestLogger.setOptUser(getOptUser(request));
			httpRequestLogger.setParams(paramMap);
			httpRequestLogger.setClientIpAddr(NetUtils.getRemoteIpAddr(request));
			httpRequestLogger.setServerIpAddr(NetUtils.getLocalIpAddr(request));
			httpRequestLogger.setOptEndMillis(null);
			httpRequestLogger.setProcessTime1(null);
			httpRequestLogger.setProcessTime2(null);
			httpRequestLogger.setAsynRequest(HttpUtils.isAsynRequest(request));
			httpRequestLoggerThreadLocal.set(httpRequestLogger);
			logger.debug(">>> do request logging[preHandle] : " + httpRequestLogger);
			if(handler != null && handler instanceof HandlerMethod){
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				HttpRequestLogging httpRequestLoggingAnnotation = handlerMethod.getMethodAnnotation(HttpRequestLogging.class);
				if(httpRequestLoggingAnnotation != null){
					String title = httpRequestLoggingAnnotation.title();
					if(!StringUtils.isEmpty(title)){
						Matcher matcher = messageSourceCodePattern.matcher(title);
						if(matcher.find()){
							title = getMessage(matcher.group(1));
						}
					}
					if(StringUtils.isEmpty(title)){
						title = getMessage("logging.title.default");
					}
					httpRequestLogger.setTitle(title);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return true;
	}

	/**
	 * Action方法执行完毕以后调用
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HttpRequestLogger<?> httpRequestLogger = httpRequestLoggerThreadLocal.get();
		if (httpRequestLogger != null) {
			try {
				httpRequestLogger.setOptEndMillis(System.currentTimeMillis());
				httpRequestLogger.setProcessTime1(httpRequestLogger.getOptEndMillis() - httpRequestLogger.getOptBeginMillis());
				httpRequestLogger.setLoggingCompleted(true);
				logger.debug(">>> do request logging[postHandle] : " + httpRequestLogger);
				if(handler != null && handler instanceof HandlerMethod){
					HandlerMethod handlerMethod = (HandlerMethod) handler;
					HttpRequestLogging httpRequestLoggingAnnotation = handlerMethod.getMethodAnnotation(HttpRequestLogging.class);
					if(httpRequestLoggingAnnotation != null && httpRequestLoggingAnnotation.persistIntoDatabase() && httpRequestLogger.getOptUser() != null){
						httpRequestLoggingExecutor.execute(createHttpRequestLoggerHandler(httpRequestLogger));
					}
				}
			} finally {
				httpRequestLoggerThreadLocal.remove();
			}
		}
	}

	/**
	 * 渲染页面完成以后调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	public void destroy() throws Exception {
		httpRequestLoggingExecutor.shutdown();
	}
	
	/**
	 * 获取操作人的LoginUser对象
	 * @param request
	 * @return
	 */
	protected abstract <T> T getOptUser(HttpServletRequest request);
	
	/**
	 * 从国际化资源文件中获取message
	 * @param code
	 * @return
	 */
	protected abstract String getMessage(String code);
	
	/**
	 * 创建HttpRequestLogger处理器(如将日志写入数据库、日志文件等等)
	 * @return
	 */
	protected abstract <T> AbstractHttpRequestLoggerHandler<T> createHttpRequestLoggerHandler(HttpRequestLogger<T> httpRequestLogger);
	
	protected String getStringParameterValue(String[] values){
		if(values == null){
			return null;
		}else{
			return values.length == 1 ? values[0] : Arrays.toString(values);
		}
	}
	
}
