package com.certusnet.xproject.common.web.interceptor;

import java.util.Map;

/**
 * 对Http请求的日志记录bean
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:39
 * @version  	1.0
 */
public class HttpRequestLogger<T> {

	/**
	 * 日志标题
	 * 由注解@HttpRequestLogging(title=${xxxx})从国际化资源文件中获取
	 */
	private String title;
	
	/**
	 * 请求URI
	 */
	private String uri;
	
	/**
	 * 请求方法(GET/POST/PUT/DELETE/INPUT)
	 */
	private String method;
	
	/**
	 * 请求参数列表(字符串形式)
	 */
	private Map<String,String> params;
	
	/**
	 * 访问者
	 */
	private T optUser;
	
	/**
	 * 访问时间
	 */
	private String optTime;
	
	/**
	 * 访问者ip地址
	 */
	private String clientIpAddr;
	
	/**
	 * 被访问的服务器地址+端口号
	 */
	private String serverIpAddr;

	/**
	 * 访问开始系统毫秒数
	 */
	private Long optBeginMillis;
	
	/**
	 * 访问结束系统毫秒数
	 */
	private Long optEndMillis;
	
	/**
	 * 从请求到达至控制器处理完毕业务逻辑准备渲染页面时的这段操作时长(毫秒)
	 * 理论上processTime1 < processTime2
	 */
	private Long processTime1;
	
	/**
	 * 从请求到达至渲染页面完毕时的这段操作时长(毫秒)
	 * 理论上processTime1 < processTime2
	 */
	private Long processTime2;
	
	/**
	 * 日志记录是否结束
	 */
	private boolean isLoggingCompleted = false;

	/**
	 * 请求是否是异步的(ajax请求)
	 */
	private boolean isAsynRequest = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public T getOptUser() {
		return optUser;
	}

	public void setOptUser(T optUser) {
		this.optUser = optUser;
	}

	public String getOptTime() {
		return optTime;
	}

	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}

	public String getClientIpAddr() {
		return clientIpAddr;
	}

	public void setClientIpAddr(String clientIpAddr) {
		this.clientIpAddr = clientIpAddr;
	}

	public String getServerIpAddr() {
		return serverIpAddr;
	}

	public void setServerIpAddr(String serverIpAddr) {
		this.serverIpAddr = serverIpAddr;
	}

	public Long getOptBeginMillis() {
		return optBeginMillis;
	}

	public void setOptBeginMillis(Long optBeginMillis) {
		this.optBeginMillis = optBeginMillis;
	}

	public Long getOptEndMillis() {
		return optEndMillis;
	}

	public void setOptEndMillis(Long optEndMillis) {
		this.optEndMillis = optEndMillis;
	}

	public Long getProcessTime1() {
		return processTime1;
	}

	public void setProcessTime1(Long processTime1) {
		this.processTime1 = processTime1;
	}

	public Long getProcessTime2() {
		return processTime2;
	}

	public void setProcessTime2(Long processTime2) {
		this.processTime2 = processTime2;
	}

	public boolean isLoggingCompleted() {
		return isLoggingCompleted;
	}

	public void setLoggingCompleted(boolean isLoggingCompleted) {
		this.isLoggingCompleted = isLoggingCompleted;
	}

	public boolean isAsynRequest() {
		return isAsynRequest;
	}

	public void setAsynRequest(boolean isAsynRequest) {
		this.isAsynRequest = isAsynRequest;
	}

	public String toString() {
		return "HttpRequestLogger [title=" + title + ", uri=" + uri
				+ ", method=" + method + ", params=" + params + ", optUser="
				+ optUser + ", optTime=" + optTime + ", clientIpAddr="
				+ clientIpAddr + ", serverIpAddr=" + serverIpAddr
				+ ", optBeginMillis=" + optBeginMillis + ", optEndMillis="
				+ optEndMillis + ", processTime1=" + processTime1
				+ ", processTime2=" + processTime2 + ", isLoggingCompleted="
				+ isLoggingCompleted + ", isAsynRequest=" + isAsynRequest + "]";
	}

}
