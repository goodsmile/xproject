package com.certusnet.xproject.admin.web.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.common.web.shiro.authz.CustomAuthorizationInfo;
import com.certusnet.xproject.common.web.shiro.realm.AbstractPrincipalRealm;
/**
 * 自定义的用户Realm
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月28日 上午10:24:49
 * @version  	1.0
 */
public class AdminUserRealm extends AbstractPrincipalRealm<AdminUser,AdminResource> {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserRealm.class);
	
	/**
	 * 用户授权-用户有哪些角色、权限
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info(">>> 【用户授权】principals = {}", principals);
		String userName = (String)principals.getPrimaryPrincipal();
		AdminUser user = getPrincipalService().getPrincipalObject(userName);
		CustomAuthorizationInfo<AdminResource> authorizationInfo = new CustomAuthorizationInfo<AdminResource>();
        authorizationInfo.setRoles(getPrincipalService().getRoles(user));
        authorizationInfo.setStringPermissions(getPrincipalService().getPermissions(user));
        authorizationInfo.setResources(getPrincipalService().getResources(user));
        return authorizationInfo;
	}

	/**
	 * 用户认证-验证用户是否登录、用户名密码是否匹配
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info(">>> 【用户认证】token = {}", token);
		String userName = (String)token.getPrincipal();
		AdminUser user = getPrincipalService().getPrincipalObject(userName);
        if(user == null) {
            throw new UnknownAccountException("Unknown account: " + userName);//没找到帐号
        }
        if(AdminUserStatusEnum.ADMIN_USER_STATUS_DISABLED.getStatusCode().equals(user.getStatus())) {
            throw new LockedAccountException("Account[" + userName + "] has been locked!"); //帐号锁定
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserName(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getPasswordSalt()),//salt
                getName()  //realm name
        );
        return authenticationInfo;
	}

	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return principals.toString();
    }
	
}
