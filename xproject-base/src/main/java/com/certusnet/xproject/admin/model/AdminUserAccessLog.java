package com.certusnet.xproject.admin.model;

/**
 * 后天管理用户操作日志
 * 
 * @author 	pengpeng
 * @date   		2017年5月13日 下午12:19:28
 * @version 	1.0
 */
public class AdminUserAccessLog {

	private Long id;
	
	/**
	 * 日志标题
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
	 * 请求头
	 */
	private String requestHeader;
	
	/**
	 * 请求体类型
	 */
	private String requestContentType;
	
	/**
	 * 请求参数
	 */
	private String requestParameter;
	
	/**
	 * 访问者
	 */
	private Long accessUserId;
	
	/**
	 * 访问时间
	 */
	private String accessTime;
	
	/**
	 * 访问者ip地址
	 */
	private String clientIpAddr;
	
	/**
	 * 被访问的服务器地址+端口号
	 */
	private String serverIpAddr;

	/**
	 * 控制器方法的执行时长(毫秒)
	 */
	private Long processTime1;
	
	/**
	 * 控制器方法执行完毕到页面渲染时长(毫秒)
	 */
	private Long processTime2;
	
	/**
	 * 日志记录是否结束
	 */
	private Boolean loggingCompleted = false;

	/**
	 * 请求是否是异步的
	 */
	private Boolean asynRequest = false;
	
	/**
	 * 响应体类型
	 */
	private String responseContentType;
	
	/**
	 * 响应结果
	 */
	private String responseResult;
	
	private String createTime;
	
	//以下属于辅助字段
	private String userName;
	private String startAccessTime;
	private String endAccessTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getRequestContentType() {
		return requestContentType;
	}

	public void setRequestContentType(String requestContentType) {
		this.requestContentType = requestContentType;
	}

	public String getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(String requestParameter) {
		this.requestParameter = requestParameter;
	}

	public Long getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(Long accessUserId) {
		this.accessUserId = accessUserId;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
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

	public Boolean getLoggingCompleted() {
		return loggingCompleted;
	}

	public void setLoggingCompleted(Boolean loggingCompleted) {
		this.loggingCompleted = loggingCompleted;
	}

	public Boolean getAsynRequest() {
		return asynRequest;
	}

	public void setAsynRequest(Boolean asynRequest) {
		this.asynRequest = asynRequest;
	}

	public String getResponseContentType() {
		return responseContentType;
	}

	public void setResponseContentType(String responseContentType) {
		this.responseContentType = responseContentType;
	}

	public String getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStartAccessTime() {
		return startAccessTime;
	}

	public void setStartAccessTime(String startAccessTime) {
		this.startAccessTime = startAccessTime;
	}

	public String getEndAccessTime() {
		return endAccessTime;
	}

	public void setEndAccessTime(String endAccessTime) {
		this.endAccessTime = endAccessTime;
	}

	@Override
	public String toString() {
		return "AdminUserAccessLog [id=" + id + ", title=" + title + ", uri=" + uri + ", method=" + method
				+ ", requestHeader=" + requestHeader + ", requestParameter=" + requestParameter + ", accessUserId="
				+ accessUserId + ", accessTime=" + accessTime + ", clientIpAddr=" + clientIpAddr + ", serverIpAddr="
				+ serverIpAddr + ", processTime1=" + processTime1 + ", processTime2=" + processTime2
				+ ", loggingCompleted=" + loggingCompleted + ", asynRequest=" + asynRequest + ", createTime="
				+ createTime + "]";
	}
	
}
