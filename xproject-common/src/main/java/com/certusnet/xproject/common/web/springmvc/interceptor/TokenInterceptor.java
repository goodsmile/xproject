package com.certusnet.xproject.common.web.springmvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.certusnet.xproject.common.util.StringUtils;

/**
 * 针对SpringMVC框架的防表单重复提交拦截器
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月26日 下午4:08:42
 * @version  	1.0
 */
public class TokenInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
	
	public static final String DEFAULT_ERROR_MESSAGE = "请不要重复提交表单!";
	
	private ApplicationContext applicationContext;
	
	private ViewResolver viewResolver;
	
	private String errorMessageCode;
	
	private TokenValueDAO tokenValueDAO = new DefaultTokenValueDAO();
	
	protected ViewResolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(ViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	protected String getErrorMessageCode() {
		return errorMessageCode;
	}

	public void setErrorMessageCode(String errorMessageCode) {
		this.errorMessageCode = errorMessageCode;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void afterPropertiesSet() throws Exception {
		if(viewResolver == null){
			viewResolver = applicationContext.getBean(ViewResolver.class);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected TokenValueDAO getTokenValueDAO() {
		return tokenValueDAO;
	}

	public void setTokenValueDAO(TokenValueDAO tokenValueDAO) {
		this.tokenValueDAO = tokenValueDAO;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			Method method = handlerMethod.getMethod();
			Token token = method.getAnnotation(Token.class);
			if(token != null){
				String[] keys = token.key();
				if(token.action() == TokenAction.CREATE){
					if(keys != null && keys.length > 0){
						for(String key : keys){
							String tokenValue = createTokenValue(request);
							setTokenValue(request, key, tokenValue);
							logger.info(">>> create token[{}={}] for current http request[{}]", key, tokenValue, request.getRequestURI());
						}
					}
				}
				if(token.action() == TokenAction.CHECK){
					if(keys != null && keys.length > 0){
						for(String key : keys){
							String tokenValue = getTokenValue(request, key);
							boolean isRepeatSubmit = false;
							if(tokenValue == null){ //重复提交
								logger.error(">>> repeat submit found for current http request[{}]", request.getRequestURI());
								isRepeatSubmit = true;
							}else if(!tokenValue.equals(request.getParameter(key))){ //重复提交
								logger.error(">>> repeat submit found for current http request[{}]", request.getRequestURI());
								isRepeatSubmit = true;
							}
							if(isRepeatSubmit){
								handleRepeatSubmit(request, response, handler);
								return false;
							}
						}
						for(String key : keys){
							removeTokenValue(request, key);
						}
					}
				}
			}
			return true;
		}else{
			return super.preHandle(request, response, handler);
		}
	}
	
	/**
	 * 创建token值
	 * @param request
	 * @return
	 */
	protected String createTokenValue(HttpServletRequest request){
		return getTokenValueDAO().createTokenValue(request);
	}
	
	/**
	 * 设置新生成的token值
	 * 例如：设置新生成的token值到HttpSession回话中去,或者放到Memecached缓存中去(分布式情况下)
	 * @param request
	 * @param key
	 * @param value
	 */
	protected void setTokenValue(HttpServletRequest request, String key, String value){
		getTokenValueDAO().setTokenValue(request, key, value);
	}
	
	/**
	 * 获取刚刚设置的token值
	 * @param request
	 * @param key
	 * @return
	 */
	protected String getTokenValue(HttpServletRequest request, String key){
		return getTokenValueDAO().getTokenValue(request, key);
	}
	
	/**
	 * 删除token值
	 * @param request
	 * @param key
	 */
	protected void removeTokenValue(HttpServletRequest request, String key){
		getTokenValueDAO().removeTokenValue(request, key);
	}
	
	/**
	 * 处理重复提交
	 * @param request
	 * @param response
	 * @param handler
	 */
	protected void handleRepeatSubmit(HttpServletRequest request, HttpServletResponse response, Object handler){
		throw new RepeatSubmitException(getErrorMessage(request));
	}
	
	/**
	 * 获取错误消息
	 * 默认：DEFAULT_ERROR_MESSAGE,如果指定了errorMessageCode则根据errorMessageCode从资源文件中找
	 * @param request
	 * @return
	 */
	protected String getErrorMessage(HttpServletRequest request){
		if(StringUtils.isEmpty(errorMessageCode)){
			return DEFAULT_ERROR_MESSAGE;
		}else{
			LocaleResolver localeResolver = (LocaleResolver) request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
			String message = applicationContext.getMessage(errorMessageCode, null, localeResolver.resolveLocale(request));
			if(StringUtils.isEmpty(message) && applicationContext.getParent() != null){
				message = applicationContext.getParent().getMessage(errorMessageCode, null, localeResolver.resolveLocale(request));
			}
			return StringUtils.defaultIfEmpty(message, DEFAULT_ERROR_MESSAGE);
		}
	}
	
}
