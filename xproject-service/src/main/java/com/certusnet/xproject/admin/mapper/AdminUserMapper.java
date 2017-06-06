package com.certusnet.xproject.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.common.support.DefaultDatabase;
import com.certusnet.xproject.common.support.OrderBy;

/**
 * 后台管理用户DAO
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午3:02:16
 * @version 	1.0
 */
@DefaultDatabase
public interface AdminUserMapper {
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
    public void updateUserStatus(@Param("userId")Long userId, @Param("status")Integer status);
    
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
    public void updateLoginTime(@Param("userId")Long userId, @Param("lastLoginTime")String lastLoginTime);
    
    /**
     * 根据用户id获取用户信息
     * 
     * @param userId
     * @return
     */
    public AdminUser selectUserById(Long userId);
    
    /**
     * 根据用户id获取用户信息[仅userId,userName,password,createTime有值]
     * 
     * @param userId
     * @return
     */
    public AdminUser selectThinUserById(Long userId);
    
    /**
     * 根据[用户名、用户状态、用户类型]查询用户列表(分页、排序)
     * 
     * @param condition
     * @param orderBy
     * @param rowBounds
     * @return
     */
    public List<AdminUser> selectUserList(@Param("condition")AdminUser condition, @Param("orderBy")OrderBy orderBy, RowBounds rowBounds);
    
    public int countUserList(@Param("condition")AdminUser condition);
    
    /**
     * 获取用户所拥有的角色
     * 
     * @param userId
     * @param filterParam
     * @return
     */
    public List<AdminRole> selectUserRoleList(@Param("userId")Long userId, @Param("filterParam")AdminRole filterParam);
    
    /**
     * 获取用户所能访问的URL资源
     * 
     * @param userId
     * @return
     */
    public List<AdminResource> selectUserResourceList(Long userId);
    
    /**
     * 添加用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     * @param optUserId - 操作人id
     * @param optTime - 操作时间
     */
    public void insertUserRoles(@Param("userId")Long userId, @Param("roleIdList")List<Long> roleIdList, @Param("optUserId")Long optUserId, @Param("optTime")String optTime);
    
    /**
     * 删除用户-角色关系
     * 
     * @param userId
     * @param roleIdList
     */
    public void deleteUserRoles(@Param("userId")Long userId, @Param("roleIdList")List<Long> roleIdList);
    
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
    public AdminUser selectUserByUserName(@Param("userName")String userName, @Param("fatUser")boolean fatUser);
}
