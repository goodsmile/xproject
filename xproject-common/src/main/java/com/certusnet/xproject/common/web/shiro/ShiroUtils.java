package com.certusnet.xproject.common.web.shiro;

import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;

import com.certusnet.xproject.common.util.CollectionUtils;

public class ShiroUtils {

	/**
	 * 获取Shiro会话
	 * @return
	 */
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}
	
	/**
	 * 获取Shiro会话中的attribute值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(String key) {
		return (T) getSession().getAttribute(key);
	}
	
	/**
	 * 设置Shiro会话中的attribute值
	 * @return
	 */
	public static void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRealm(Class<? extends Realm> realmType){
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		if(!CollectionUtils.isEmpty(securityManager.getRealms())){
			for(Iterator<Realm> it = securityManager.getRealms().iterator(); it.hasNext();){
				Realm realm = it.next();
				if(realm.getClass().equals(realmType)){
					return (T) realm;
				}
			}
		}
		return null;
	}
	
}
