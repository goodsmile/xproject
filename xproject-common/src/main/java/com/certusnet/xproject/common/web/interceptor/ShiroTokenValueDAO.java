package com.certusnet.xproject.common.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.certusnet.xproject.common.web.shiro.ShiroUtils;
/**
 * 基于Shiro会话上下文的token DAO
 * 如果shiro的用户会话支持分布式session,那么该token值也是支持分布式的
 * 
 * @author  pengpeng
 * @date 	 2015年6月10日 上午9:29:32
 * @version 1.0
 */
public class ShiroTokenValueDAO implements TokenValueDAO {

	public String createTokenValue(HttpServletRequest request) {
		String uuid =  UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}

	public void setTokenValue(HttpServletRequest request, String key, String value) {
		ShiroUtils.getSession().setAttribute(key, value);
	}

	public String getTokenValue(HttpServletRequest request, String key) {
		return (String) ShiroUtils.getSession().getAttribute(key);
	}

	public void removeTokenValue(HttpServletRequest request, String key) {
		ShiroUtils.getSession().removeAttribute(key);
	}

}
