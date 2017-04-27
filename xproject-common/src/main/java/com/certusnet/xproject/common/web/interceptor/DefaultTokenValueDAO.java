package com.certusnet.xproject.common.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
/**
 * 默认的基于Servlet容器上下文的token DAO
 * 
 * @author  pengpeng
 * @date 	 2015年6月10日 上午9:29:32
 * @version 1.0
 */
public class DefaultTokenValueDAO implements TokenValueDAO {

	public String createTokenValue(HttpServletRequest request) {
		String uuid =  UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}

	public void setTokenValue(HttpServletRequest request, String key, String value) {
		request.getSession().setAttribute(key, value);
	}

	public String getTokenValue(HttpServletRequest request, String key) {
		return (String) request.getSession().getAttribute(key);
	}

	public void removeTokenValue(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}

}
