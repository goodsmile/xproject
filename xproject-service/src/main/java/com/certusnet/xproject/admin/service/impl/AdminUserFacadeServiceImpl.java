package com.certusnet.xproject.admin.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.shiro.service.PrincipalService;
import com.certusnet.xproject.common.web.shiro.service.ShiroCacheService;

@Lazy
@Service("adminUserFacadeService")
public class AdminUserFacadeServiceImpl implements AdminUserService, PrincipalService<AdminUser, AdminResource>
{
    @Resource(name = "shiroCacheService")
    private ShiroCacheService shiroCacheService;
    
    @Resource(name = "adminUserService")
    private AdminUserService delegate;
    
    public void createUser(AdminUser user) {
        delegate.createUser(user);
    }
    
    public void updateUser(AdminUser user) {
        delegate.updateUser(user);
        shiroCacheService.crearCachedSessionInfo(user.getUserName()); // 清除会话缓存,迫使其重新登录
        shiroCacheService.clearCachedAuthenticationInfo(user.getUserName()); // 清除认证缓存
        shiroCacheService.clearCachedAuthorizationInfo(user.getUserName()); // 此处最好加上清除逻辑
    }
    
    public void updatePassword(AdminUser user, boolean forceUpdate) {
        delegate.updatePassword(user, forceUpdate);
        shiroCacheService.crearCachedSessionInfo(user.getUserName()); // 清除会话缓存,迫使其重新登录
        shiroCacheService.clearCachedAuthenticationInfo(user.getUserName()); // 密码变更涉及到了登录认证,需要重新登录
    }
    
    public void deleteUserById(AdminUser user) {
        delegate.deleteUserById(user);
        shiroCacheService.crearCachedSessionInfo(user.getUserName()); // 清除会话缓存
        shiroCacheService.clearCachedAuthorizationInfo(user.getUserName()); // 用户被删除,清除有关于他的一切缓存
        shiroCacheService.clearCachedAuthenticationInfo(user.getUserName()); // 用户被删除,清除有关于他的一切缓存
    }
    
    public void updateUserStatus(AdminUser user) {
        delegate.updateUserStatus(user);
        shiroCacheService.crearCachedSessionInfo(user.getUserName()); // 清除会话缓存,迫使其重新登录
        shiroCacheService.clearCachedAuthorizationInfo(user.getUserName()); // 用户被删除,清除有关于他的一切缓存
        shiroCacheService.clearCachedAuthenticationInfo(user.getUserName()); // 用户账户状态被改变(例如:由正常->锁定),需要清除其认证缓存
    }
    
    public void updateLoginTime(Long userId, String lastLoginTime) {
        delegate.updateLoginTime(userId, lastLoginTime);
    }
    
    public AdminUser getUserById(Long userId) {
        return delegate.getUserById(userId);
    }
    
    public List<AdminUser> getUserList(AdminUser user, Pager pager, OrderBy orderby) {
        return delegate.getUserList(user, pager, orderby);
    }
    
    public List<AdminRole> getUserRoleList(Long userId) {
        return delegate.getUserRoleList(userId);
    }
    
    public List<AdminResource> getUserResourceList(Long userId) {
        return delegate.getUserResourceList(userId);
    }
    
    public void addUserRoles(AdminUser user, List<Long> roleIdList, Long optUserId, String optTime) {
        delegate.addUserRoles(user, roleIdList, optUserId, optTime);
        shiroCacheService.clearCachedAuthorizationInfo(user.getUserName()); // 用户角色发生变动,清除有关于他的一切缓存
    }
    
    public void delUserRoles(AdminUser user, List<Long> roleIdList) {
        delegate.delUserRoles(user, roleIdList);
        shiroCacheService.clearCachedAuthorizationInfo(user.getUserName()); // 用户角色发生变动,清除有关于他的一切缓存
    }
    
    public AdminUser getUserByUserName(String userName, boolean fatUser) {
        return delegate.getUserByUserName(userName, fatUser);
    }
    
    public Set<String> getRoles(AdminUser principal) {
        Set<String> userRoleCodes = new LinkedHashSet<String>();
        List<AdminRole> userRoleList = getUserRoleList(principal.getUserId());
        if (!CollectionUtils.isEmpty(userRoleList)) {
            for (AdminRole role : userRoleList) {
                userRoleCodes.add(role.getRoleCode());
            }
        }
        return userRoleCodes;
    }
    
    public Set<String> getPermissions(AdminUser principal) {
        Set<String> userPermissions = new LinkedHashSet<String>();
        List<AdminResource> userResourceList = getUserResourceList(principal.getUserId());
        if (!CollectionUtils.isEmpty(userResourceList)) {
            for (AdminResource resource : userResourceList) {
                if (!StringUtils.isEmpty(resource.getPermissionExpression())) {
                    userPermissions.add(resource.getPermissionExpression());
                }
            }
        }
        return userPermissions;
    }
    
    public Set<AdminResource> getResources(AdminUser principal) {
        List<AdminResource> userResourceList = getUserResourceList(principal.getUserId());
        return new LinkedHashSet<AdminResource>(userResourceList);
    }
    
    public AdminUser getPrincipalObject(String principal) {
        return getUserByUserName(principal, false);
    }
    
}
