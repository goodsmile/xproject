package com.certusnet.xproject.common.web.springmvc.interceptor;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.AbstractSpringTypedBeanManager;
import com.certusnet.xproject.common.support.HttpAccessLogging;
import com.certusnet.xproject.common.support.HttpAccessLogging.LoggingType;
import com.certusnet.xproject.common.support.Messages;
import com.certusnet.xproject.common.support.NamedThreadFactory;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.JsonUtils;
import com.certusnet.xproject.common.util.NetUtils;
import com.certusnet.xproject.common.util.SpringMvcUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.springmvc.interceptor.HttpAccessLog.HttpRequestParameter;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Http请求日志记录拦截器
 * 
 * @author 	pengpeng
 * @date   		2017年5月20日 下午4:16:20
 * @version 	1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHttpAccessLoggingInterceptor extends AbstractSpringTypedBeanManager<HttpAccessLogDAO,HttpAccessLogging.LoggingType> implements HandlerInterceptor, DisposableBean {

	private static final Pattern messageSourceCodePattern = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)\\}");
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpAccessLoggingInterceptor.class);
	
	private static final ExecutorService httpAccessLogHandlerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("HTTP-REQUEST-LOGGING-EXECUTE-WORKER-"));
	
	private static final ThreadLocal<LoggingContext> loggingContextThreadLocal = new NamedThreadLocal<LoggingContext>("LoggingContext ThreadLocal");
	
	private static final String DEFAULT_LOG_TITLE = "用户访问日志";
	
	private String defaultLogTitle = DEFAULT_LOG_TITLE;
	
	private boolean loggingRequestHeader = false;
	
	public String getDefaultLogTitle() {
		return defaultLogTitle;
	}

	public void setDefaultLogTitle(String defaultLogTitle) {
		this.defaultLogTitle = defaultLogTitle;
	}

	public boolean isLoggingRequestHeader() {
		return loggingRequestHeader;
	}

	public void setLoggingRequestHeader(boolean loggingRequestHeader) {
		this.loggingRequestHeader = loggingRequestHeader;
	}

	public static ExecutorService getHttpAccessLogHandlerExecutor() {
		return httpAccessLogHandlerExecutor;
	}

	public static ThreadLocal<LoggingContext> getLoggingContextThreadLocal() {
		return loggingContextThreadLocal;
	}

	/**
	 * 请求进入处理器方法之前调用
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			String requestURI = request.getRequestURI();
			//logger.debug(requestURI);
			if(handler != null && handler instanceof HandlerMethod){
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				HttpAccessLogging httpAccessLogging = handlerMethod.getMethodAnnotation(HttpAccessLogging.class);
				if(httpAccessLogging != null){
					HttpAccessLog<Object> httpAccessLog = new HttpAccessLog<Object>();
					LoggingContext loggingContext = new LoggingContext();
					loggingContext.setHandlerMethod(handlerMethod);
					loggingContext.setHttpAccessLogging(httpAccessLogging);
					loggingContext.setHttpAccessLog(httpAccessLog);
					httpAccessLog.setTitle(getLogTitle(httpAccessLogging.title()));
					httpAccessLog.setAccessBeginMillis(System.currentTimeMillis());
					httpAccessLog.setUri(requestURI);
					httpAccessLog.setAccessTime(DateTimeUtils.formatNow());
					httpAccessLog.setMethod(request.getMethod());
					httpAccessLog.setClientIpAddr(NetUtils.getRemoteIpAddr(request));
					httpAccessLog.setServerIpAddr(NetUtils.getLocalIpAddr(request));
					httpAccessLog.setRequestContentType(getContentType(request.getContentType()));
					httpAccessLog.setAccessEndMillis(null);
					httpAccessLog.setProcessTime1(null);
					httpAccessLog.setProcessTime2(null);
					httpAccessLog.setAsynRequest(SpringMvcUtils.isAsyncRequest(request, handler));
					if(isLoggingRequestHeader()){
						httpAccessLog.setRequestHeader(extractRequestHeader(request, loggingContext));
					}
					getLoggingContextThreadLocal().set(loggingContext);
					//logger.debug(">>> access logging [preHandle] : " + httpAccessLog);
				}
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}

	/**
	 * Action方法执行完毕以后调用
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		LoggingContext loggingContext = getLoggingContextThreadLocal().get();
		if(loggingContext != null && handler.equals(loggingContext.getHandlerMethod())){
			HttpAccessLog<?> httpAccessLog = loggingContext.getHttpAccessLog();
			if(httpAccessLog != null){
				httpAccessLog.setAccessEndMillis(System.currentTimeMillis());
				httpAccessLog.setProcessTime1(httpAccessLog.getAccessEndMillis() - httpAccessLog.getAccessBeginMillis());
				httpAccessLog.setRequestParameter(extractRequestParameter(request, loggingContext));
				httpAccessLog.setAccessUser(getAccessUser(request, loggingContext));
				//logger.debug(">>> access logging [postHandle] : " + httpAccessLog);
			}
		}
	}

	/**
	 * 渲染页面完成以后调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			LoggingContext loggingContext = getLoggingContextThreadLocal().get();
			if(loggingContext != null && handler.equals(loggingContext.getHandlerMethod())){
				HttpAccessLog<?> httpAccessLog = loggingContext.getHttpAccessLog();
				if(httpAccessLog != null){
					long nowTimeMillis = System.currentTimeMillis();
					httpAccessLog.setProcessTime2(nowTimeMillis - httpAccessLog.getAccessEndMillis());
					httpAccessLog.setAccessEndMillis(nowTimeMillis);
					httpAccessLog.setLoggingCompleted(true);
					httpAccessLog.setResponseContentType(getContentType(response.getContentType()));
					httpAccessLog.setResponseResult(extractResponseResult(response, loggingContext));
					HttpAccessLogDAO httpAccessLogDAO = getHttpAccessLogDAO(loggingContext);
					if(httpAccessLogDAO != null){
						getHttpAccessLogHandlerExecutor().submit(new DefaultHttpAccessLoggingTask(request, response, loggingContext, httpAccessLogDAO));
					}
				}
			}
		} finally {
			getLoggingContextThreadLocal().remove();
		}
	}

	/**
	 * 获取日志标题
	 * @param title
	 * @return
	 */
	protected String getLogTitle(String title) {
		if(!StringUtils.isEmpty(title)){
			Matcher matcher = messageSourceCodePattern.matcher(title);
			if(matcher.find()){
				title = getMessage(matcher.group(1));
			}
		}
		return StringUtils.defaultIfEmpty(title, defaultLogTitle);
	}
	
	/**
	 * 从国际化资源文件中获取message
	 * @param code
	 * @return
	 */
	protected String getMessage(String code) {
		return Messages.getMessage(code);
	}
	
	protected String getStringParameterValue(String[] values){
		if(values == null){
			return null;
		}else{
			return values.length == 1 ? values[0] : Arrays.toString(values);
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求参数(包括请求体中的参数)
	 * @param request
	 * @param loggingContext
	 * @return
	 * @throws Exception
	 */
	protected HttpRequestParameter extractRequestParameter(HttpServletRequest request, LoggingContext loggingContext) throws Exception {
		HttpRequestParameter parameter = new HttpRequestParameter();
		Map<String,String[]> originalParamMap = request.getParameterMap();
		Map<String,String> paramMap = new HashMap<String,String>();
		if(originalParamMap != null && !originalParamMap.isEmpty()){
			for(Map.Entry<String, String[]> entry : originalParamMap.entrySet()){
				paramMap.put(entry.getKey(), getStringParameterValue(entry.getValue()));
			}
		}
		parameter.setParameter(paramMap);
		MediaType contentType = loggingContext.getHttpAccessLog().getRequestContentType();
		if(contentType != null){
			if(isContentCachingRequest(request)){
				String charset = request.getCharacterEncoding();
				if(StringUtils.isEmpty(charset)){
					charset = StringUtils.defaultIfEmpty(contentType.getCharset().name(), GlobalConstants.SYSTEM_DEFAULT_CHARSET);
				}
				ContentCachingRequestWrapper requestToUse = (ContentCachingRequestWrapper) request;
				byte[] bytes = requestToUse.getContentAsByteArray();
				if(bytes != null){
					String body = IOUtils.toString(bytes, charset);
					Object bodyObj = body;
					if(MediaType.APPLICATION_JSON.getType().equals(contentType.getType())){ //目前只处理JSON类型的数据
						if(body.startsWith("[") && body.endsWith("]")){ //JSON Array String -> List<Map<String,Object>>
							bodyObj = JsonUtils.json2Object(body, new TypeReference<List<Map<String,Object>>>(){});
						}else if(body.startsWith("{") && body.endsWith("}")){ //JSON Object String -> Map<String,Object>
							bodyObj = JsonUtils.json2Object(body, new TypeReference<Map<String,Object>>(){});
						}
					}
					parameter.setBody(bodyObj);
				}
				
			}
		}
		return excludeRequestParameter(parameter, loggingContext);
	}
	
	/**
	 * 记录请求参数时剔除一些敏感数据,如用户密码明文
	 * @param parameter
	 * @param loggingContext
	 * @return
	 */
	protected HttpRequestParameter excludeRequestParameter(HttpRequestParameter parameter, LoggingContext loggingContext) {
		HttpAccessLogging httpAccessLogging = loggingContext.httpAccessLogging;
		String[] excludeNameParams = httpAccessLogging.excludeParamNames();
		if(excludeNameParams != null && excludeNameParams.length > 0){
			try {
				for(String paramName : excludeNameParams){
					if(parameter.getParameter() != null){
						parameter.getParameter().remove(paramName);
					}
					MediaType contentType = loggingContext.getHttpAccessLog().getRequestContentType();
					if (contentType != null && parameter.getBody() != null) {
						if(parameter.getBody() instanceof List){ //JSON Array
							for(Map<String,Object> item : (List<Map<String,Object>>)parameter.getBody()){
								excludeParameter(item, paramName);
							}
						}else if(parameter.getBody() instanceof Map){ //JSON Object
							excludeParameter((Map<String,Object>)parameter.getBody(), paramName);
						}
					}
				}
			} catch (Exception e) {
				logger.error(">>> 排除参数出错! " + e.getMessage());
			}
		}
		return parameter;
	}
	
	/**
	 * 递归剔除参数
	 * @param parameter
	 * @param paramName
	 */
	protected void excludeParameter(Map<String,Object> parameter, String paramName) {
		if(!CollectionUtils.isEmpty(parameter)){
			parameter.remove(paramName);
			for(Map.Entry<String,Object> entry : parameter.entrySet()){
				Object value = entry.getValue();
				if(value != null){
					if(value instanceof List){
						List<Map<String,Object>> list = (List<Map<String, Object>>) value;
						for(Map<String,Object> item : list){
							excludeParameter(item, paramName);
						}
					}else if(value instanceof Map){
						Map<String,Object> item = (Map<String, Object>) value;
						excludeParameter(item, paramName);
					}
				}
			}
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求头信息
	 * @param request
	 * @param loggingContext
	 * @return
	 */
	protected Map<String,String> extractRequestHeader(HttpServletRequest request, LoggingContext loggingContext) {
		Map<String,String> headerMap = new HashMap<String,String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		if(headerNames != null){
			while(headerNames.hasMoreElements()){
				String headerName = headerNames.nextElement();
				headerMap.put(headerName, request.getHeader(headerName));
			}
		}
		return headerMap;
	}
	
	/**
	 * 提取请求结果
	 * @param request
	 * @param loggingContext
	 * @return
	 */
	protected Object extractResponseResult(HttpServletResponse response, LoggingContext loggingContext) throws Exception {
		MediaType contentType = loggingContext.getHttpAccessLog().getResponseContentType();
		if(contentType != null){
			if(isContentCachingResponse(response)){
				String charset = response.getCharacterEncoding();
				if(StringUtils.isEmpty(charset)){
					charset = StringUtils.defaultIfEmpty(contentType.getCharset().name(), GlobalConstants.SYSTEM_DEFAULT_CHARSET);
				}
				ContentCachingResponseWrapper responseToUse = (ContentCachingResponseWrapper) response;
				byte[] bytes = responseToUse.getContentAsByteArray();
				if(bytes != null){
					return IOUtils.toString(bytes, charset);
				}
			}
		}
		return null;
	}
	
	protected MediaType getContentType(String contentType) {
		try {
			return MediaType.valueOf(contentType);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected boolean isContentCachingRequest(ServletRequest request) {
		if(request instanceof ContentCachingRequestWrapper){
			return true;
		}else if(request instanceof HttpServletRequestWrapper) {
			HttpServletRequestWrapper requestToUse = (HttpServletRequestWrapper) request;
			return isContentCachingRequest(requestToUse.getRequest());
		}
		return false;
	}
	
	protected boolean isContentCachingResponse(ServletResponse response) {
		if(response instanceof ContentCachingResponseWrapper){
			return true;
		}else if(response instanceof HttpServletResponseWrapper) {
			HttpServletResponseWrapper responseToUse = (HttpServletResponseWrapper) response;
			return isContentCachingResponse(responseToUse.getResponse());
		}
		return false;
	}
	
	public void destroy() throws Exception {
		getHttpAccessLogHandlerExecutor().shutdown();
	}
	
	protected boolean filterBean(HttpAccessLogDAO httpAccessLogDAO, LoggingType parameter) {
		if(parameter != null){
			return parameter.equals(httpAccessLogDAO.getLoggingType());
		}
		return false;
	}

	/**
	 * 获取操作人的LoginUser对象
	 * @param request
	 * @param loggingContext
	 * @return
	 */
	protected abstract <T> T getAccessUser(HttpServletRequest request, LoggingContext loggingContext);
	
	/**
	 * 创建HttpRequestLogger处理器(如将日志写入数据库、日志文件等等)
	 * @param loggingContext
	 * @return
	 */
	protected HttpAccessLogDAO getHttpAccessLogDAO(LoggingContext loggingContext) {
		return getTypedBean(loggingContext.getHttpAccessLogging().loggingType());
	}
	
	/**
	 * 日志记录上下文
	 * 
	 * @author 	pengpeng
	 * @date   		2017年5月18日 下午1:16:18
	 * @version 	1.0
	 */
	public static class LoggingContext {
		
		private HandlerMethod handlerMethod;
		
		private HttpAccessLogging httpAccessLogging;
		
		private HttpAccessLog<?> httpAccessLog;
		
		private ModelAndView modelAndView;

		public HandlerMethod getHandlerMethod() {
			return handlerMethod;
		}

		public void setHandlerMethod(HandlerMethod handlerMethod) {
			this.handlerMethod = handlerMethod;
		}

		public HttpAccessLogging getHttpAccessLogging() {
			return httpAccessLogging;
		}

		public void setHttpAccessLogging(HttpAccessLogging httpAccessLogging) {
			this.httpAccessLogging = httpAccessLogging;
		}

		public HttpAccessLog<?> getHttpAccessLog() {
			return httpAccessLog;
		}

		public void setHttpAccessLog(HttpAccessLog<?> httpAccessLog) {
			this.httpAccessLog = httpAccessLog;
		}
		
		public ModelAndView getModelAndView() {
			return modelAndView;
		}

		public void setModelAndView(ModelAndView modelAndView) {
			this.modelAndView = modelAndView;
		}

	}
	
	@SuppressWarnings("unused")
	public class DefaultHttpAccessLoggingTask implements Runnable {

		private final HttpServletRequest request;
		
		private final HttpServletResponse response;
		
		private final LoggingContext loggingContext;
		
		private final HttpAccessLogDAO httpAccessLogDAO;
		
		public DefaultHttpAccessLoggingTask(HttpServletRequest request, HttpServletResponse response, LoggingContext loggingContext, HttpAccessLogDAO httpAccessLogDAO) {
			super();
			this.request = request;
			this.response = response;
			this.loggingContext = loggingContext;
			this.httpAccessLogDAO = httpAccessLogDAO;
		}

		public void run() {
			try {
				HttpAccessLog<?> httpAccessLog = loggingContext.getHttpAccessLog();
				if (httpAccessLog != null) {
					logger.debug(">>> http access log : " + httpAccessLog);
					httpAccessLogDAO.saveLog((HttpAccessLog<?>) httpAccessLog);
				} 
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
	}
	
}
