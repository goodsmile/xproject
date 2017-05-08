package com.certusnet.xproject.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.consts.em.AdminUserTypeEnum;
import com.certusnet.xproject.admin.dao.AdminUserDAO;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.BusinessAssert;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;
import com.certusnet.xproject.common.support.ValidationAssert;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.web.shiro.UserPasswdUtils;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
	
    @Resource(name = "adminUserDAO")
    private AdminUserDAO userDAO;
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createUser(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        try {
            user.setEncryptedPassword(UserPasswdUtils.encryptPassword(user.getPassword(), user.getPasswordSalt(), GlobalConstants.DEFAULT_PASSWORD_HASH_ITERATIONS));
            userDAO.insertUser(user);
        }
        catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUser(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        try {
            userDAO.updateUser(user);
        }
        catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updatePassword(AdminUser user, boolean forceUpdate) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        ValidationAssert.notEmpty(user.getPassword(), "新密码不能为空!");
        ValidationAssert.notEmpty(user.getRepassword(), "重复新密码不能为空!");
        ValidationAssert.isTrue(user.getPassword().equals(user.getRepassword()), "两次输入新密码不一致!");
        AdminUser puser = userDAO.getThinUserById(user.getUserId());
        ValidationAssert.notNull(user, "该用户已经不存在了!");
        if (!forceUpdate) {
            ValidationAssert.notNull(user.getOldpassword(), "旧密码不能为空!");
            String encryptedOldpassword = UserPasswdUtils.encryptPassword(user.getOldpassword(), puser.getPasswordSalt(), GlobalConstants.DEFAULT_PASSWORD_HASH_ITERATIONS);
            ValidationAssert.isTrue(puser.getPassword().equals(encryptedOldpassword), "旧密码不正确!");
        }
        user.setUserName(puser.getUserName());
        user.setCreateTime(puser.getCreateTime());
        user.setEncryptedPassword(UserPasswdUtils.encryptPassword(user.getPassword(), user.getPasswordSalt(), GlobalConstants.DEFAULT_PASSWORD_HASH_ITERATIONS));
        user.setUpdateTime(DateTimeUtils.formatNow());
        userDAO.updatePassword(user);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteUserById(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = userDAO.getThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        BusinessAssert.isTrue(!AdminUserTypeEnum.ADMIN_USER_TYPE_SYSTEM.getTypeCode().equals(puser.getUserType()), "系统级用户不能被删除!");
        user.setUserName(puser.getUserName());
        userDAO.deleteUserById(user.getUserId());
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUserStatus(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUserStatusEnum emStatus = AdminUserStatusEnum.getStatus(user.getStatus());
        ValidationAssert.notNull(emStatus, String.format("无法识别的用户状态(status=%s)!", user.getStatus()));
        AdminUser puser = userDAO.getThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        userDAO.updateUserStatus(user.getUserId(), emStatus);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateLoginTime(Long userId, String lastLoginTime) {
        ValidationAssert.notNull(userId, "用户id不能为空!");
        ValidationAssert.notEmpty(lastLoginTime, "登录时间不能为空!");
        userDAO.updateLoginTime(userId, lastLoginTime);
    }
    
    public AdminUser getUserById(Long userId) {
        return userDAO.getUserById(userId);
    }
    
    public PagingList<AdminUser> getUserList(AdminUser user, Pager pager, OrderBy orderby) {
    	List<AdminUser> dataList = userDAO.getUserList(user, pager, orderby);
    	return new PagingList<AdminUser>(dataList, pager.getTotalRowCount());
    }
    
    public List<AdminRole> getUserRoleList(Long userId, AdminRole filterParam) {
        return userDAO.getUserRoleList(userId, filterParam);
    }
    
    public List<AdminRole> getUserRoleList(Long userId) {
        return userDAO.getUserRoleList(userId, null);
    }
    
    public List<AdminResource> getUserResourceList(Long userId) {
        return userDAO.getUserResourceList(userId);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addUserRoles(AdminUser user, List<Long> roleIdList, Long optUserId, String optTime) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = userDAO.getThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        userDAO.insertUserRoles(user.getUserId(), roleIdList, optUserId, optTime);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delUserRoles(AdminUser user, List<Long> roleIdList) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = userDAO.getThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        userDAO.deleteUserRoles(user.getUserId(), roleIdList);
    }
    
    public AdminUser getUserByUserName(String userName, boolean fatUser) {
        return userDAO.getUserByUserName(userName, fatUser);
    }
    
}
