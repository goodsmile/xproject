package com.certusnet.xproject.admin.web.interceptor;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;

import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.common.support.HttpAccessLogging.LoggingType;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;
import com.certusnet.xproject.common.web.springmvc.interceptor.AbstractHttpAccessLogHandler;
import com.certusnet.xproject.common.web.springmvc.interceptor.AbstractHttpAccessLoggingInterceptor;
import com.certusnet.xproject.common.web.springmvc.interceptor.HttpAccessLog;
import com.certusnet.xproject.common.web.springmvc.interceptor.HttpAccessLog.HttpRequestParameter;

@SuppressWarnings("unchecked")
public class DefaultHttpAccessLoggingInterceptor extends AbstractHttpAccessLoggingInterceptor {

	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	protected AdminUser getAccessUser(HttpServletRequest request, LoggingContext loggingContext) {
		if(loggingContext.getHttpAccessLogging().isLogin()){ //用户正在登录
			HandlerMethod handlerMethod = loggingContext.getHandlerMethod();
			MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
			if(methodParameters != null){
				for(MethodParameter methodParameter : methodParameters){
					if(methodParameter.hasParameterAnnotation(RequestBody.class) && AdminUser.class.equals(methodParameter.getParameterType())){
						HttpRequestParameter requestParameter = loggingContext.getHttpAccessLog().getRequestParameter();
						Object requestBody = requestParameter.getBody();
						MediaType contentType = loggingContext.getHttpAccessLog().getRequestContentType();
						if (contentType != null && requestBody != null && requestBody instanceof Map && MediaType.APPLICATION_JSON.getType().equals(contentType.getType())) {
							Map<String,Object> requestBodyMap = (Map<String, Object>) requestBody;
							return adminUserService.getUserByUserName(MapUtils.getString(requestBodyMap, "userName"), false);
						}
					}
				}
			}
			return null;
		}else{ //用户已登录
			LoginToken<AdminUser> loginToken = (LoginToken<AdminUser>) ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
			return loginToken == null ? null : loginToken.getLoginUser();
		}
	}

	protected AbstractHttpAccessLogHandler<AdminUser> createHttpAccessLoggerHandler(LoggingContext loggingContext) {
		LoggingType loggingType = loggingContext.getHttpAccessLogging().loggingType();
		if(LoggingType.DB.equals(loggingType)){
			return new DbStoreHttpAccessLogHandler((HttpAccessLog<AdminUser>) loggingContext.getHttpAccessLog());
		}else if(LoggingType.FILE.equals(loggingType)){
			return new FileStoreHttpAccessLogHandler((HttpAccessLog<AdminUser>) loggingContext.getHttpAccessLog());
		}
		return null;
	}

}
