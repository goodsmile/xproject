package com.certusnet.xproject.admin.service.impl;

import java.util.List;
import java.util.function.Consumer;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.consts.em.AdminUserTypeEnum;
import com.certusnet.xproject.admin.mapper.AdminUserMapper;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.common.consts.ApplicationConstants;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.BusinessAssert;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;
import com.certusnet.xproject.common.support.ValidationAssert;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.ModelDecodeUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.shiro.UserPasswdUtils;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
	
	public static final Consumer<AdminUser> ADMIN_USER_DECODER = model -> {
		model.setRepassword(model.getPassword());
        if (model.getStatus() != null) {
            AdminUserStatusEnum em = AdminUserStatusEnum.getStatus(model.getStatus());
            if (em != null) {
                model.setStatusName(em.getStatusName());
            }
        }
        if (model.getUserType() != null) {
            AdminUserTypeEnum em = AdminUserTypeEnum.getUserType(model.getUserType());
            if (em != null) {
                model.setUserTypeName(em.getTypeName());
            }
        }
        if (!StringUtils.isEmpty(model.getUserIcon())) {
        	String userIconUrl = model.getUserIcon();
        	if(userIconUrl.toLowerCase().startsWith("/resources/")){ //工程目录下的本地图片
        		userIconUrl = ApplicationConstants.CONTEXT_PATH + userIconUrl;
        	}else if(userIconUrl.toLowerCase().startsWith("http")){
        		//nothing to do
        	}else{
        		userIconUrl = GlobalConstants.IMAGE_SERVER_DOMAIN + userIconUrl;
        	}
        	model.setUserIconUrl(userIconUrl);
        }
	};
	
    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createUser(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        try {
            user.setEncryptedPassword(UserPasswdUtils.encryptPassword(user.getPassword(), user.getPasswordSalt(), GlobalConstants.DEFAULT_PASSWORD_HASH_ITERATIONS));
            adminUserMapper.insertUser(user);
        }
        catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUser(AdminUser user, boolean clearShiroCache) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        try {
        	adminUserMapper.updateUser(user);
        }
        catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updatePassword(AdminUser user, boolean forceUpdate, boolean clearShiroCache) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        ValidationAssert.notEmpty(user.getPassword(), "新密码不能为空!");
        ValidationAssert.notEmpty(user.getRepassword(), "重复新密码不能为空!");
        ValidationAssert.isTrue(user.getPassword().equals(user.getRepassword()), "两次输入新密码不一致!");
        AdminUser puser = adminUserMapper.selectThinUserById(user.getUserId());
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
        adminUserMapper.updatePassword(user);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteUserById(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = adminUserMapper.selectThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        BusinessAssert.isTrue(!AdminUserTypeEnum.ADMIN_USER_TYPE_SYSTEM.getTypeCode().equals(puser.getUserType()), "系统级用户不能被删除!");
        user.setUserName(puser.getUserName());
        adminUserMapper.deleteUserById(user.getUserId()); //删除用户信息
        adminUserMapper.deleteUserAllRoles(user.getUserId()); //删除用户的所有角色关系
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUserStatus(AdminUser user) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        ValidationAssert.notNull(AdminUserStatusEnum.getStatus(user.getStatus()), String.format("无法识别的用户状态(status=%s)!", user.getStatus()));
        AdminUser puser = adminUserMapper.selectThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        adminUserMapper.updateUserStatus(user.getUserId(), user.getStatus());
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateLoginTime(Long userId, String lastLoginTime) {
        ValidationAssert.notNull(userId, "用户id不能为空!");
        ValidationAssert.notEmpty(lastLoginTime, "登录时间不能为空!");
        adminUserMapper.updateLoginTime(userId, lastLoginTime);
    }
    
    public AdminUser getUserById(Long userId) {
        return ModelDecodeUtils.decodeModel(adminUserMapper.selectUserById(userId), ADMIN_USER_DECODER);
    }
    
    public PagingList<AdminUser> getUserList(AdminUser condition, Pager pager, OrderBy orderBy) {
    	List<AdminUser> dataList = ModelDecodeUtils.decodeModel(adminUserMapper.selectUserList(condition, orderBy, new RowBounds(pager.getOffset(), pager.getLimit())), ADMIN_USER_DECODER);
    	return new PagingList<AdminUser>(dataList, adminUserMapper.countUserList(condition));
    }
    
    public List<AdminRole> getUserRoleList(Long userId, AdminRole filterParam) {
        return ModelDecodeUtils.decodeModel(adminUserMapper.selectUserRoleList(userId, filterParam), AdminRoleServiceImpl.ADMIN_ROLE_DECODER);
    }
    
    public List<AdminRole> getUserRoleList(Long userId) {
        return getUserRoleList(userId, null);
    }
    
    public List<AdminResource> getUserResourceList(Long userId) {
        return ModelDecodeUtils.decodeModel(adminUserMapper.selectUserResourceList(userId), AdminResourceServiceImpl.ADMIN_RESOURCE_DECODER);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addUserRoles(AdminUser user, List<Long> roleIdList, Long optUserId, String optTime) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = adminUserMapper.selectThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        adminUserMapper.insertUserRoles(user.getUserId(), roleIdList, optUserId, optTime);
    }
    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delUserRoles(AdminUser user, List<Long> roleIdList) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户id不能为空!");
        AdminUser puser = adminUserMapper.selectThinUserById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        adminUserMapper.deleteUserRoles(user.getUserId(), roleIdList);
    }
    
    public AdminUser getUserByUserName(String userName, boolean fatUser) {
        return ModelDecodeUtils.decodeModel(adminUserMapper.selectUserByUserName(userName, fatUser), ADMIN_USER_DECODER);
    }
    
}
