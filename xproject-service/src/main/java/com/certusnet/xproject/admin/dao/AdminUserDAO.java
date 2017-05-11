package com.certusnet.xproject.admin.dao;

import java.util.List;

import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;

/**
 * 后台管理用户DAO
 * 
 * @author pengpeng
 * @date 2015年12月10日 下午1:49:33
 * @version 1.0
 */
public interface AdminUserDAO
{
    /**
     * 新增用户
     * 
     * @param user
     */
    public void insertUser(AdminUser user);
    
    /**
     * 更新用户
     * 
     * @param user
     */
    public void updateUser(AdminUser user);
    
    /**
     * 删除用户
     * 
     * @param userId
     */
    public void deleteUserById(Long userId);
    
    /**
     * 更新用户状态(禁用/启用账户)
     * 
     * @param userId
     * @param status
     */
    public void updateUserStatus(Long userId, AdminUserStatusEnum status);
    
    /**
     * 用户修改密码
     * 
     * @param user
     */
    public void updatePassword(AdminUser user);
    
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
     * 根据用户id获取用户信息[仅userId,userName,password,createTime有值]
     * 
     * @param userId
     * @return
     */
    public AdminUser getThinUserById(Long userId);
    
    /**
     * 根据[用户名、用户状态、用户类型]查询用户列表(分页、排序)
     * 
     * @param user
     * @param pager
     * @param orderby
     * @return
     */
    public List<AdminUser> getUserList(AdminUser user, Pager pager, OrderBy orderby);
    
    /**
     * 获取用户所拥有的角色
     * 
     * @param userId
     * @param filterParam
     * @return
     */
    public List<AdminRole> getUserRoleList(Long userId, AdminRole filterParam);
    
    /**
     * 获取用户所能访问的URL资源
     * 
     * @param userId
     * @return
     */
    public List<AdminResource> getUserResourceList(Long userId);
    
    /**
     * 添加用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     * @param optUserId - 操作人id
     * @param optTime - 操作时间
     */
    public void insertUserRoles(Long userId, List<Long> roleIdList, Long optUserId, String optTime);
    
    /**
     * 删除用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     */
    public void deleteUserRoles(Long userId, List<Long> roleIdList);
    
    /**
     * 删除用户的所有角色
     * 
     * @param userId
     * @param roleIdList
     */
    public void deleteUserAllRoles(Long userId);
    
    /**
     * 根据用户名获取用户信息
     * 
     * @param userName
     * @param fatUser -
     *            true:查询User[全部信息],false:仅查出User[userId,userName,password,
     *            status,createTime]
     * @return
     */
    public AdminUser getUserByUserName(String userName, boolean fatUser);
}
