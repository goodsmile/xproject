package com.certusnet.xproject.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.common.support.DefaultDatabase;
import com.certusnet.xproject.common.support.OrderBy;

/**
 * 系统角色DAO
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午2:44:19
 * @version 	1.0
 */
@DefaultDatabase
public interface AdminRoleMapper {

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
	public AdminRole selectThinRoleById(Long roleId);
	
	/**
	 * 根据角色id获取角色详情
	 * @param roleId
	 */
	public AdminRole selectRoleById(Long roleId);
	
	/**
	 * 根据【角色名称、角色代码】查询角色列表(分页、排序)
     * @param condition
     * @param orderBy
     * @param rowBounds
	 * @return
	 */
	public List<AdminRole> selectRoleList(@Param("condition")AdminRole condition, @Param("orderBy")OrderBy orderBy, RowBounds rowBounds);
	
	public int countRoleList(@Param("condition")AdminRole condition);
	
	/**
	 * 获取该角色的看见资源
	 * @param roleId
	 * @return
	 */
	public List<AdminResource> selectResourceListByRoleId(Long roleId);
	
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
	public void insertRoleResources(@Param("roleId")Long roleId, @Param("resourceIdList")List<Long> resourceIdList, @Param("optUserId")Long optUserId, @Param("optTime")String optTime);
	
}
