package com.certusnet.xproject.admin.service;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;

/**
 * 后台管理用户Service
 * 
 * @author pengpeng
 * @date 2015年12月10日 下午2:05:07
 * @version 1.0
 */
public interface AdminUserService
{
    /**
     * 新增用户
     * 
     * @param user
     */
    public void createUser(AdminUser user);
    
    /**
     * 更新用户
     * 
     * @param user
     * @param clearShiroCache - 是否清除shiro中的缓存信息(session, auth)
     */
    public void updateUser(AdminUser user, boolean clearShiroCache);
    
    /**
     * 用户修改密码
     * 
     * @param user
     * @param forceUpdate - 是否强制更新密码
     * @param clearShiroCache - 是否清除shiro中的缓存信息(session, auth)
     */
    public void updatePassword(AdminUser user, boolean forceUpdate, boolean clearShiroCache);
    
    /**
     * 删除用户
     * 
     * @param user
     */
    public void deleteUserById(AdminUser user);
    
    /**
     * 更新用户状态(禁用/启用账户)
     * 
     * @param userId
     * @param status
     */
    public void updateUserStatus(AdminUser user);
    
    /**
     * 更新用户登录时间
     * 
     * @param userId
     * @param lastLoginTime
     */
    public void updateLoginTime(Long userId, String lastLoginTime);
    
    /**
     * 根据用户id获取用户信息
     * 
     * @param userId
     * @return
     */
    public AdminUser getUserById(Long userId);
    
    /**
     * 根据[用户名、用户状态、用户类型]查询用户列表(分页、排序)
     * 
     * @param condition
     * @param pager
     * @param orderBy
     * @return
     */
    public PagingList<AdminUser> getUserList(AdminUser condition, Pager pager, OrderBy orderBy);
    
    /**
     * 获取用户所拥有的角色
     * 
     * @param userId
     * @param filterParam
     * @return
     */
    public List<AdminRole> getUserRoleList(Long userId, AdminRole filterParam);
    
    /**
     * 获取用户所拥有的角色
     * 
     * @param userId
     * @return
     */
    public List<AdminRole> getUserRoleList(Long userId);
    
    /**
     * 获取用户所能访问的URL资源
     * 
     * @param userId
     * @return
     */
    public List<AdminResource> getUserResourceList(Long userId);
    
    /**
     * 添加用户-角色配置
     * 
     * @param user
     * @param roleIdList
     * @param optUserId - 操作人id
     * @param optTime - 操作时间
     */
    public void addUserRoles(AdminUser user, List<Long> roleIdList, Long optUserId, String optTime);
    
    /**
     * 删除用户-角色配置
     * 
     * @param user
     * @param roleIdList
     */
    public void delUserRoles(AdminUser user, List<Long> roleIdList);
    
    /**
     * 根据用户名获取用户信息
     * 
     * @param userName
     * @param fatUser - true:查询User[全部信息],false:仅查出User[userId,userName,password, createTime]
     * @return
     */
    public AdminUser getUserByUserName(String userName, boolean fatUser);
}
