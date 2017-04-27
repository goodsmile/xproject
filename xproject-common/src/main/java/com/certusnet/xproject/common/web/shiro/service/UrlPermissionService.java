package com.certusnet.xproject.common.web.shiro.service;

import java.util.Map;
/**
 * 系统中定义的全部url=perm配置服务
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月28日 下午5:20:41
 * @version  	1.0
 */
public interface UrlPermissionService {

	/**
	 * 获取系统中定义的全部url=perm配置
	 * @return
	 */
	public Map<String,String> getAllUrlPermissions();
	
	/**
	 * 发布通知所有集群重新加载全局(URL-权限)配置
	 * @return
	 */
	public void pubGlobalUrlPermissionsReload();
	
	/**
	 * 订阅重新加载全局(URL-权限)配置的通知
	 * @return
	 */
	public void subGlobalUrlPermissionsReload();
	
	/**
     * 重新加载系统的全局(URL-权限)配置
     */
    public void reloadGlobalUrlPermissions();
	
}
