package com.certusnet.xproject.common.web.shiro.session;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

public class CustomWebSessionManager extends DefaultWebSessionManager {

	/**
	 * 去掉URL后面的jsessionid
	 */
	protected void onStart(Session session, SessionContext context) {  
        super.onStart(session, context);  
        ServletRequest request = WebUtils.getRequest(context);  
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);  
    }
	
}
