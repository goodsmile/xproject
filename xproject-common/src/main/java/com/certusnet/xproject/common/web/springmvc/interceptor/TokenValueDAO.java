package com.certusnet.xproject.common.web.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
/**
 * token值DAO
 * 
 * @author  pengpeng
 * @date 	 2015年6月10日 上午9:27:40
 * @version 1.0
 */
public interface TokenValueDAO {

	/**
	 * 创建token值
	 * @param request
	 * @return
	 */
	public String createTokenValue(HttpServletRequest request);
	
	/**
	 * 设置token值
	 * @param request
	 * @param key
	 * @param value
	 */
	public void setTokenValue(HttpServletRequest request, String key, String value);

	/**
	 * 获取token值
	 * @param request
	 * @param key
	 * @return
	 */
	public String getTokenValue(HttpServletRequest request, String key);
	
	/**
	 * 删除token值
	 * @param request
	 * @param key
	 */
	public void removeTokenValue(HttpServletRequest request, String key);
	
}
