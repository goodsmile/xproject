package com.certusnet.xproject.admin.web.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.certusnet.xproject.admin.web.shiro.realm.AdminUserRealm;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;
import com.certusnet.xproject.common.web.shiro.service.ShiroCacheService;

@Lazy
@Service("shiroCacheService")
public class ShiroCacheServiceImpl implements ShiroCacheService {

	private static final Logger logger = LoggerFactory.getLogger(ShiroCacheServiceImpl.class);
	
	@Autowired  
    private CachingSessionDAO sessionDAO;
	
	public void clearCachedAuthorizationInfo(String principal) {
		try {
			AdminUserRealm userRealm = ShiroUtils.getRealm(AdminUserRealm.class);
			if(userRealm != null){
				PrincipalCollection principals = new SimplePrincipalCollection(principal, userRealm.getName());
				userRealm.clearCachedAuthorizationInfo(principals);
				logger.warn(">>> 清除用户[{}]的授权信息缓存", principal);
			}
		} catch (Exception e) {
			logger.error(String.format(">>> 清除用户授权缓存发生异常: %s", e.getMessage()), e);
		}
	}

	public void clearCachedAuthenticationInfo(String principal) {
		try {
			AdminUserRealm userRealm = ShiroUtils.getRealm(AdminUserRealm.class);
			if(userRealm != null){
				PrincipalCollection principals = new SimplePrincipalCollection(principal, userRealm.getName());
				userRealm.clearCachedAuthenticationInfo(principals);
				logger.warn(">>> 清除用户[{}]的认证信息缓存", principal);
			}
		} catch (Exception e) {
			logger.error(String.format(">>> 清除用户认证缓存发生异常: %s", e.getMessage()), e);
		}
	}

	public void crearCachedSessionInfo(String principal) {
		try {
			logger.warn(">>> 清除用户[{}]的会话缓存", principal);
			String cacheName = sessionDAO.getActiveSessionsCacheName();
			Cache<String,Object> cache = sessionDAO.getCacheManager().getCache(cacheName);
			/**
			 * @see #SimpleSessionDAO
			 */
			String sessionId = (String) cache.get(principal);
			if(!StringUtils.isEmpty(sessionId)){
				cache.remove(sessionId);
				cache.remove(principal);
			}
		} catch (Exception e) {
			logger.error(String.format(">>> 清除用户[%s]的会话缓存发生异常: %s", principal, e.getMessage()), e);
		}
	}

	public void clearAllCachedAuthorizationInfo() {
		try {
			AdminUserRealm userRealm = ShiroUtils.getRealm(AdminUserRealm.class);
			if(userRealm != null){
				userRealm.clearAllCachedAuthorizationInfo();
				logger.warn(">>> 清除所有用户的授权信息缓存");
			}
		} catch (Exception e) {
			logger.error(String.format(">>> 清除用户认证缓存发生异常: %s", e.getMessage()), e);
		}
	}

	public void clearAllCachedAuthenticationInfo() {
		try {
			AdminUserRealm userRealm = ShiroUtils.getRealm(AdminUserRealm.class);
			if(userRealm != null){
				userRealm.clearAllCachedAuthenticationInfo();
				logger.warn(">>> 清除所有用户的认证信息缓存");
			}
		} catch (Exception e) {
			logger.error(String.format(">>> 清除用户认证缓存发生异常: %s", e.getMessage()), e);
		}
	}

	public void crearAllCachedSessionInfo() {
		try {
			logger.warn(">>> 清除所有用户的会话缓存");
			sessionDAO.getCacheManager().getCache(sessionDAO.getActiveSessionsCacheName()).clear();
		} catch (Exception e) {
			logger.error(String.format(">>> 清除所有用户的会话缓存发生异常: %s", e.getMessage()), e);
		}
	}

}
