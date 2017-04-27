package com.certusnet.xproject.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.certusnet.xproject.admin.consts.em.AdminRoleTypeEnum;
import com.certusnet.xproject.admin.dao.AdminRoleDAO;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.common.mybatis.DefaultBaseMybatisDAO;
import com.certusnet.xproject.common.mybatis.ModelHandler;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;

@Repository("adminRoleDAO")
public class AdminRoleDAOImpl extends DefaultBaseMybatisDAO implements AdminRoleDAO {

	public void insertRole(AdminRole role) {
		this.getSqlSessionTemplate().insert(getMapperKey("insertRole"), role);
	}

	public void updateRole(AdminRole role) {
		this.getSqlSessionTemplate().update(getMapperKey("updateRole"), role);
	}
	
	public void deleteRoleById(Long roleId) {
		this.getSqlSessionTemplate().delete(getMapperKey("deleteRoleById"), roleId);
	}
	
	public AdminRole getThinRoleById(Long roleId) {
		return this.getSqlSessionTemplate().selectOne(getMapperKey("selectThinRoleById"), roleId);
	}

	public AdminRole getRoleById(Long roleId) {
		return this.getSqlSessionTemplate().selectOne(getMapperKey("selectRoleById"), roleId, new AdminRoleModelHandler());
	}

	public List<AdminRole> getRoleList(AdminRole role, Pager pager, OrderBy orderby) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("roleName", role.getRoleName());
		paramMap.put("roleCode", role.getRoleCode());
		paramMap.put("orderby", orderby.getOrderby());
		paramMap.put("order", orderby.getOrder());
		return this.getSqlSessionTemplate().selectList(getMapperKey("selectRoleList"), paramMap, new AdminRoleModelHandler(), pager);
	}
	
	public List<AdminResource> getResourceListByRoleId(Long roleId) {
		return this.getSqlSessionTemplate().selectList(getMapperKey("selectResourceListByRoleId"), roleId);
	}

	public void deleteRoleResourcesByRoleId(Long roleId) {
		this.getSqlSessionTemplate().delete(getMapperKey("deleteRoleResourcesByRoleId"), roleId);
	}
	
	public void insertRoleResources(Long roleId, List<Long> resourceIdList, Long optUserId, String optTime) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("roleId", roleId);
		paramMap.put("resourceIdList", resourceIdList);
		paramMap.put("createBy", optUserId);
		paramMap.put("createTime", optTime);
		this.getSqlSessionTemplate().insert(getMapperKey("insertRoleResources"), paramMap);
	}
	
	protected Class<?> getBoundModelClass() {
		return AdminRole.class;
	}

	public static class AdminRoleModelHandler implements ModelHandler<AdminRole> {

		public void handleModel(AdminRole element) {
			if(element.getRoleType() != null){
				AdminRoleTypeEnum em = AdminRoleTypeEnum.getType(element.getRoleType());
				if(em != null){
					element.setRoleTypeName(em.getTypeName());
				}
			}
		}
		
	}
	
}