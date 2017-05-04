package com.certusnet.xproject.admin.service;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;

/**
 * 系统角色Service
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月3日 上午10:35:28
 * @version  	1.0
 */
public interface AdminRoleService {

	/**
	 * 创建角色
	 * @param role
	 */
	public void createRole(AdminRole role);
	
	/**
	 * 修改角色
	 * @param role
	 */
	public void updateRole(AdminRole role);
	
	/**
	 * 根据角色id删除角色
	 * @param roleId
	 */
	public void deleteRoleById(Long roleId);
	
	/**
	 * 根据角色id获取角色详情
	 * @param roleId
	 */
	public AdminRole getRoleById(Long roleId);
	
	/**
	 * 根据【角色名称、角色代码】查询角色列表(分页、排序)
	 * @param role
	 * @param pager
	 * @param orderby
	 * @return
	 */
	public PagingList<AdminRole> getRoleList(AdminRole role, Pager pager, OrderBy orderby);
	
	/**
	 * 获取该角色的看见资源
	 * @param roleId
	 * @return
	 */
	public List<AdminResource> getResourceListByRoleId(Long roleId);
	
	/**
	 * 配置该角色的可见资源
	 * @param roleId
	 * @param resourceIdList
	 * @param optUserId
	 * @param optTime
	 */
	public void configRoleResources(Long roleId, List<Long> resourceIdList, Long optUserId, String optTime);
	
}
