package com.certusnet.xproject.common.web.shiro.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.certusnet.xproject.common.support.Result;
import com.certusnet.xproject.common.util.HttpUtils;
import com.certusnet.xproject.common.util.JsonUtils;

/**
 * 通过setDelegateFilter(null) 来被动创建新的delegateFilter,创建delegateFilter时需要重新加载dynamicUrlPermissions配置
 * 从而实现分布式环境下动态URL-权限配置的需求
 * 
 * @author  pengpeng
 * @date 	 2016年3月1日 下午6:29:12
 * @version 1.0
 */
public class DynamicUrlPermissionsShiroFilter implements Filter, ApplicationContextAware {

	private static final Object lock = new Object();
	
	private ApplicationContext applicationContext;
	
	private volatile Filter delegateShiroFilter;
	
	protected Filter getDelegateShrioFilter() {
		if(delegateShiroFilter == null){
			synchronized (lock) {
				if(delegateShiroFilter == null){
					delegateShiroFilter = createDelegateFilter();
				}
			}
		}
		return delegateShiroFilter;
	}

	public void setDelegateShiroFilter(Filter delegateShiroFilter) {
		this.delegateShiroFilter = delegateShiroFilter;
	}
	
	public void reloadGlobalUrlPermissions() {
		synchronized (lock) {
			setDelegateShiroFilter(null);
			getDelegateShrioFilter();
		}
	}
	
	protected Filter createDelegateFilter() {
		return (Filter) applicationContext.getBean(AbstractShiroFilter.class);
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		getDelegateShrioFilter().init(filterConfig);
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			getDelegateShrioFilter().doFilter(request, response, chain);
		} catch (UnknownSessionException e) {
			System.out.println(e);
			if(HttpUtils.isAsynRequest((HttpServletRequest) request)){
				Result<Object> result = new Result<Object>();
				result.setSuccess(false);
				result.setMessage("对不起,您的会话已过期,请重新登录！");
				HttpUtils.asynOutputResponse((HttpServletResponse) response, ContentType.APPLICATION_JSON, JsonUtils.object2Json(result));
			}else{
				throw e;
			}
		}
	}

	public void destroy() {
		getDelegateShrioFilter().destroy();
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
