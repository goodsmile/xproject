package com.certusnet.xproject.common.web.shiro.service;

import java.util.Set;

/**
 * 针对Principal当事人的一些方法(例如获取用户[当事人]的角色集合、获取用户[当事人]的权限结合等..)
 * @param <P>	- 一般指用户账户
 * @param <R>  - 一般指用户所拥有的资源
 * @author	  	pengpeng
 * @date	  	2015年1月30日 上午10:39:49
 * @version  	1.0
 */
public interface PrincipalService<P,R> {

	/**
	 * 根据principal获取当事人的角色代码集
	 * @param principal			- 当事人
	 * @return
	 */
	public Set<String> getRoles(P principal);
	
	/**
	 * 根据principal获取当事人权限代码集
	 * @param principal			- 当事人
	 * @return					- 返回用户所拥有的权限
	 */
	public Set<String> getPermissions(P principal);
	
	/**
	 * 根据principal获取当事人权限代码集
	 * @param principal			- 当事人
	 * @return					- 返回用户所拥有的资源
	 */
	public Set<R> getResources(P principal);
	
	/**
	 * 根据principal获取当事人的信息
	 * @param principal			- 当事人
	 * @return
	 */
	public P getPrincipalObject(String principal);

}
