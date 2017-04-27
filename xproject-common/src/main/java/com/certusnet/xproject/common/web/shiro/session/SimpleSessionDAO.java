package com.certusnet.xproject.common.web.shiro.session;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;

public class SimpleSessionDAO extends CachingSessionDAO {

    public SimpleSessionDAO() {
        setCacheManager(new AbstractCacheManager() {
            protected Cache<Serializable, Session> createCache(String name) throws CacheException {
                return new MapCache<Serializable, Session>(name, new ConcurrentHashMap<Serializable, Session>());
            }
        });
    }

    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        return null; //should never execute because this implementation relies on parent class to access cache, which
        //is where all sessions reside - it is the cache implementation that determines if the
        //cache is memory only or disk-persistent, etc.
    }

	protected void doUpdate(Session session) {
    	if(session != null){
    		try {
    			Object principalObj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    			if(principalObj != null){
    				String principal = principalObj.toString();
    				String cacheName = getActiveSessionsCacheName();
					//在cache中建立 key=principal <-> value=sessionId的映射关系,使得根据某个人的会话id清除其会话信息(强制其下线)成为可能
					getCacheManager().getCache(cacheName).put(principal, session.getId());
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }

    protected void doDelete(Session session) {
    	if(session != null){
    		try {
    			Object principalObj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    			if(principalObj != null){
    				String principal = principalObj.toString();
    				String cacheName = getActiveSessionsCacheName();
    				//删除cache中建立 key=principal <-> value=sessionId的映射关系
					getCacheManager().getCache(cacheName).remove(principal);
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }

    protected void assignSessionId(Session session, Serializable sessionId) {
        ((SimpleSession) session).setId(sessionId);
    }
    
}
