package com.certusnet.xproject.common.consts;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.certusnet.xproject.common.consts.AbstractConstants;

/**
 * 应用的全局常量,其中包括：Spring上下文对象、Servlet上下文对象、应用的上下文路径、应用系统默认字符集、默认Locale、默认日期格式等常量
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月16日 上午11:02:27
 * @version  	1.0
 */
public abstract class ApplicationConstants extends AbstractConstants {

	/**
	 * Spring的ROOT上下文,由@{link org.springframework.web.context.ContextLoaderListener}加载出来的spring上下文
	 */
	public static final ApplicationContext APPLICATION_CONTEXT = valueOf(null);
	
	/**
	 * Spring的MVC上下文,由@{link org.springframework.web.servlet.DispatcherServlet}加载出来的Spring MVC上下文
	 */
	public static final ApplicationContext MVC_APPLICATION_CONTEXT = valueOf(null);
	
	/**
	 * 应用中的Servlet上下文
	 */
	public static final ServletContext SERVLET_CONTEXT = valueOf(null); 
	
	/**
	 * 应用的上下文路径, e.g. /myapp
	 */
	public static final String CONTEXT_PATH = valueOf(null);
	
	/**
	 * 应用的上下文的实际路径, e.g. D:/Program Files/apache-tomcat-7.0.54-/webapps/myapp
	 */
	public static final String CONTEXT_REAL_PATH = valueOf(null);

}
