package com.certusnet.xproject.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.consts.em.AdminUserTypeEnum;
import com.certusnet.xproject.admin.dao.AdminUserDAO;
import com.certusnet.xproject.admin.dao.impl.AdminResourceDAOImpl.AdminResourceModelHandler;
import com.certusnet.xproject.admin.dao.impl.AdminRoleDAOImpl.AdminRoleModelHandler;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.common.consts.ApplicationConstants;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.mybatis.DefaultBaseMybatisDAO;
import com.certusnet.xproject.common.mybatis.ModelHandler;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.util.StringUtils;

@Repository("adminUserDAO")
public class AdminUserDAOImpl extends DefaultBaseMybatisDAO implements AdminUserDAO {
	
    public void insertUser(AdminUser user) {
        getSqlSessionTemplate().insert(getMapperKey("insertUser"), user);
    }
    
    public void updateUser(AdminUser user) {
        getSqlSessionTemplate().update(getMapperKey("updateUser"), user);
    }
    
    public void deleteUserById(Long userId) {
        getSqlSessionTemplate().delete(getMapperKey("deleteUserById"), userId);
    }
    
    public void updateUserStatus(Long userId, AdminUserStatusEnum status) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", status.getStatusCode());
        getSqlSessionTemplate().update(getMapperKey("updateUserStatus"), paramMap);
    }
    
    public void updatePassword(AdminUser user) {
        getSqlSessionTemplate().update(getMapperKey("updatePassword"), user);
    }
    
    public void updateLoginTime(Long userId, String lastLoginTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("lastLoginTime", lastLoginTime);
        getSqlSessionTemplate().update(getMapperKey("updateLoginTime"), paramMap);
    }
    
    public AdminUser getUserById(Long userId) {
        return getSqlSessionTemplate().selectOne(getMapperKey("getUserById"), userId, new AdminUserModelHandler());
    }
    
    public AdminUser getThinUserById(Long userId) {
        return getSqlSessionTemplate().selectOne(getMapperKey("getThinUserById"), userId);
    }
    
    public List<AdminUser> getUserList(AdminUser user, Pager pager, OrderBy orderby) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userName", user.getUserName());
        paramMap.put("realName", user.getRealName());
        paramMap.put("userType", user.getUserType());
        paramMap.put("status", user.getStatus());
        paramMap.put("orderby", orderby.getOrderby());
        paramMap.put("order", orderby.getOrder());
        return getSqlSessionTemplate().selectList(getMapperKey("getUserList"), paramMap, new AdminUserModelHandler(), pager);
    }
    
	public List<AdminRole> getUserRoleList(Long userId, AdminRole filterParam) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("filterParam", filterParam);
		return getSqlSessionTemplate().selectList(getMapperKey("getUserRoleList"), paramMap, new AdminRoleModelHandler());
	}

	public List<AdminResource> getUserResourceList(Long userId) {
        return getSqlSessionTemplate().selectList(getMapperKey("getUserResourceList"), userId, new AdminResourceModelHandler());
    }
    
    public void insertUserRoles(Long userId, List<Long> roleIdList, Long optUserId, String optTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("roleIdList", roleIdList);
        paramMap.put("createBy", optUserId);
        paramMap.put("createTime", optTime);
        getSqlSessionTemplate().insert(getMapperKey("insertUserRoles"), paramMap);
    }
    
    public void deleteUserRoles(Long userId, List<Long> roleIdList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("roleIdList", roleIdList);
        getSqlSessionTemplate().insert(getMapperKey("deleteUserRoles"), paramMap);
    }
    
    public AdminUser getUserByUserName(String userName, boolean fatUser) {
        if (fatUser) {
            return getSqlSessionTemplate().selectOne(getMapperKey("getUserByUserName"), userName, new AdminUserModelHandler());
        }
        else {
            return getSqlSessionTemplate().selectOne(getMapperKey("getThinUserByUserName"), userName, new AdminUserModelHandler());
        }
    }
    
    protected Class<?> getBoundModelClass() {
        return AdminUser.class;
    }
    
    public static class AdminUserModelHandler implements ModelHandler<AdminUser> {
    	
        public void handleModel(AdminUser element) {
            element.setRepassword(element.getPassword());
            if (element.getStatus() != null) {
                AdminUserStatusEnum em = AdminUserStatusEnum.getStatus(element.getStatus());
                if (em != null) {
                    element.setStatusName(em.getStatusName());
                }
            }
            if (element.getUserType() != null) {
                AdminUserTypeEnum em = AdminUserTypeEnum.getUserType(element.getUserType());
                if (em != null) {
                    element.setUserTypeName(em.getTypeName());
                }
            }
            if (!StringUtils.isEmpty(element.getUserIcon())) {
            	String userIconUrl = element.getUserIcon();
            	if(userIconUrl.toLowerCase().startsWith("/resources/")){ //工程目录下的本地图片
            		userIconUrl = ApplicationConstants.CONTEXT_PATH + userIconUrl;
            	}else if(userIconUrl.toLowerCase().startsWith("http")){
            		//nothing to do
            	}else{
            		userIconUrl = GlobalConstants.IMAGE_SERVER_DOMAIN + userIconUrl;
            	}
            	element.setUserIconUrl(userIconUrl);
            }
        }
    }
    
}
