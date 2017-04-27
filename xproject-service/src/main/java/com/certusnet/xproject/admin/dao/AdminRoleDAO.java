package com.certusnet.xproject.admin.dao;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;

/**
 * 系统角色DAO
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月3日 上午10:33:16
 * @version  	1.0
 */
public interface AdminRoleDAO {

	/**
	 * 插入角色
	 * @param role
	 */
	public void insertRole(AdminRole role);
	
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
	 *  根据角色id获取角色的相关信息(roleId,roleCode,roleName,inuse)
	 * @param roleId
	 * @return
	 */
	public AdminRole getThinRoleById(Long roleId);
	
	/**
	 * 根据角色id获取角色详情
	 * @param roleId
	 */
	public AdminRole getRoleById(Long roleId);
	
	/**
	 * 根据【角色名称、角色代码】查询角色列表(分页)
	 * @param role
	 * @param pager
	 * @param orderby
	 * @return
	 */
	public List<AdminRole> getRoleList(AdminRole role, Pager pager, OrderBy orderby);
	
	/**
	 * 获取该角色的看见资源
	 * @param roleId
	 * @return
	 */
	public List<AdminResource> getResourceListByRoleId(Long roleId);
	
	/**
	 * 根据角色id删除该角色下的所有资源
	 * @param roleId
	 */
	public void deleteRoleResourcesByRoleId(Long roleId);
	
	/**
	 * 批量插入角色-资源关系
	 * @param roleId
	 * @param resourceIdList
	 * @param optUserId
	 * @param optTime
	 */
	public void insertRoleResources(Long roleId, List<Long> resourceIdList, Long optUserId, String optTime);
	
}
