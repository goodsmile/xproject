package com.certusnet.xproject.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.service.AdminRoleService;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.web.shiro.service.ShiroCacheService;

@Lazy
@Service("adminRoleFacadeService")
public class AdminRoleFacadeServiceImpl implements AdminRoleService {

	@Resource(name="shiroCacheService")
	private ShiroCacheService shiroCacheService;
	
	@Resource(name="adminRoleService")
	private AdminRoleService delegate;
	
	public void createRole(AdminRole role) {
		delegate.createRole(role);
	}

	public void updateRole(AdminRole role) {
		delegate.updateRole(role);
		shiroCacheService.clearAllCachedAuthorizationInfo(); //可能角色名发生改变,需要清除所有授权缓存
	}

	public void deleteRoleById(Long roleId) {
		delegate.deleteRoleById(roleId);
		shiroCacheService.clearAllCachedAuthorizationInfo(); //有角色被删除,需要清除所有授权缓存
	}

	public AdminRole getRoleById(Long roleId) {
		return delegate.getRoleById(roleId);
	}

	public List<AdminRole> getRoleList(AdminRole role, Pager pager, OrderBy orderby) {
		return delegate.getRoleList(role, pager, orderby);
	}

	public List<AdminResource> getResourceListByRoleId(Long roleId) {
		return delegate.getResourceListByRoleId(roleId);
	}

	public void configRoleResources(Long roleId, List<Long> resourceIdList, Long optUserId, String optTime) {
		delegate.configRoleResources(roleId, resourceIdList, optUserId, optTime);
		shiroCacheService.clearAllCachedAuthorizationInfo(); //权限发生变动,需要清除所有授权缓存
	}

}
