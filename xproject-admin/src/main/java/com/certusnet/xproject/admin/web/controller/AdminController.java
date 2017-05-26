package com.certusnet.xproject.admin.web.controller;

import static com.certusnet.xproject.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.certusnet.xproject.admin.consts.em.AdminResourceActionTypeEnum;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.admin.support.AdminResourceNavMenuNodeConverter;
import com.certusnet.xproject.admin.support.AdminResourceTreeBuilder;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.admin.web.shiro.realm.AdminUserRealm;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.AbstractXTreeBuilder;
import com.certusnet.xproject.common.support.HttpAccessLogging;
import com.certusnet.xproject.common.support.Result;
import com.certusnet.xproject.common.support.TreeNodeConverter;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.ExceptionUtils;
import com.certusnet.xproject.common.util.NetUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.BaseController;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;
import com.certusnet.xproject.common.web.shiro.authz.CustomAuthorizationInfo;
/**
 * 管理后台Controller
 * 
 * @author  pengpeng
 * @date 	 2015年4月5日 下午8:44:00
 * @version 1.0
 */
@Controller
@SuppressWarnings("unchecked")
public class AdminController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private AbstractXTreeBuilder<Long, AdminResource> resourceTreeBuilder = new AdminResourceTreeBuilder();
	
	private TreeNodeConverter<AdminResource,Map<String,Object>> resourceNavMenuNodeConverter = new AdminResourceNavMenuNodeConverter();
	
	@Resource(name="adminUserFacadeService")
	private AdminUserService adminUserService;
	
	@Resource(name="adminResourceService")
	private AdminResourceService adminResourceService;
	
	@RequestMapping(value="/login", method=GET)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		logger.info(">>> 前往用户登录页面");
		return "login.html";
	}
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param loginUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login/submit", method=POST, consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	@HttpAccessLogging(title="用户登录", isLogin=true, excludeParamNames={"password"})
	public Object submitLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminUser loginUser) {
		logger.info(">>> 执行用户登录, loginUser = " + loginUser.getUserName());
		Result<Object> result = new Result<Object>();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(loginUser.getUserName(), loginUser.getPassword());
		//token.setRememberMe(true);
		try {
			currentUser.login(token);
			if(currentUser.isAuthenticated()){ //用户是否登录成功
				String nowTime = DateTimeUtils.formatNow();
				AdminUser user = adminUserService.getUserByUserName(token.getUsername(), true);
				adminUserService.updateLoginTime(user.getUserId(), nowTime);
				List<AdminRole> userRoles = adminUserService.getUserRoleList(user.getUserId());
				StringBuilder loginRoleNames = new StringBuilder();
				if(!CollectionUtils.isEmpty(userRoles)){
					for(AdminRole role : userRoles){
						loginRoleNames.append(role.getRoleName() + ",");
					}
					loginRoleNames.deleteCharAt(loginRoleNames.length() - 1);
				}
				LoginToken<AdminUser> loginToken = new LoginToken<AdminUser>();
				loginToken.setLoginId(user.getUserId());
				loginToken.setLoginName(user.getUserName());
				loginToken.setLoginAddrIp(NetUtils.getRemoteIpAddr(request));
				loginToken.setLoginTime(DateTimeUtils.formatNow());
				loginToken.setLoginTimes(user.getLoginTimes());
				loginToken.setLastLoginTime(StringUtils.defaultIfEmpty(user.getLastLoginTime(), nowTime));
				loginToken.setLoginUserIcon(user.getUserIconUrl());
				loginToken.setLoginRoleNames(loginRoleNames.toString());
				user.setLastLoginTime(nowTime);
				
				loginToken.setLoginUser(user);
				ShiroUtils.getSession().setAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY, loginToken);
				logger.info(">>> 用户登录成功! {} = {}", LoginToken.LOGIN_TOKEN_SESSION_KEY, ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY));
				result.setSuccess(true);
				result.setMessage("登录成功!");
				return result;
			}else{
				logger.info(">>> 用户登录失败!");
				result.setSuccess(false);
				result.setMessage("登录失败!");
				return result;
			}
		} catch (IncorrectCredentialsException e) {
			logger.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("用户名或密码不正确!");
			return result;
		} catch (UnknownAccountException e) {
			logger.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("用户名或密码不正确!");
			return result;
		} catch (LockedAccountException e) {
			logger.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("对不起,该账户被锁定!");
			return result;
		} catch (ExcessiveAttemptsException e) {
			logger.error(e.getMessage(), e);
			String maxPasswordRetry = e.getMessage();
			result.setSuccess(false);
			result.setMessage(String.format("对不起,您已重试密码次数达到%s次,请稍后再试!", maxPasswordRetry));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(String.format("登录出错!(错误信息:%s)", ExceptionUtils.getRootCauseMessage(e)));
			return result;
		}
	}
	
	/**
	 * 去首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"/", "/index"}, method=GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		LoginToken<AdminUser> loginToken = (LoginToken<AdminUser>) ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		logger.info(">>> 前往首页, loginUser = " + loginToken.getLoginName());
		return "index.html";
	}
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login/user/current", method=GET, produces=APPLICATION_JSON)
	public Object getLoginUserInfo(HttpServletRequest request, HttpServletResponse response) {
		LoginToken<AdminUser> loginToken = (LoginToken<AdminUser>) ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		Map<String,Object> user = new HashMap<String,Object>();
		user.put("userId", loginToken.getLoginId());
		user.put("userName", loginToken.getLoginName());
		user.put("userIconUrl", loginToken.getLoginUserIcon());
		user.put("userRoleNames", loginToken.getLoginRoleNames());
		user.put("lastLoginTime", loginToken.getLastLoginTime());
		user.put("messages", new ArrayList<Object>()); //TODO
		user.put("notices", new ArrayList<Object>()); //TODO
		return genSuccessResult(user);
	}
	
	/**
	 * 获取当前登录者所能看见的菜单集合
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login/user/menus", method=GET, produces=APPLICATION_JSON)
	public Object getLoginUserMenuList(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		try {
			List<AdminResource> userMenuResources = new ArrayList<AdminResource>();
			AdminUserRealm realm = ShiroUtils.getRealm(AdminUserRealm.class);
			AuthorizationInfo authInfo = realm.getAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
			if(authInfo instanceof CustomAuthorizationInfo){
				CustomAuthorizationInfo<AdminResource> authorizationInfo = (CustomAuthorizationInfo<AdminResource>) authInfo;
				Set<AdminResource> userResources = authorizationInfo.getResources();
				if(!CollectionUtils.isEmpty(userResources)){
					for(AdminResource resource : userResources){
						if(AdminResourceActionTypeEnum.ADMIN_RESOURCE_ACTION_TYPE_MENU.getTypeCode().equals(resource.getActionType())){
							userMenuResources.add(resource);
						}
					}
					dataList = resourceTreeBuilder.buildObjectTree(GlobalConstants.DEFAULT_ADMIN_ROOT_RESOURCE_ID, userMenuResources, resourceNavMenuNodeConverter);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dataList;
	}
	
	/**
	 * 用户登出系统
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/logout", method=GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		logger.info(">>> 用户退出系统");
		if (SecurityUtils.getSubject().getSession() != null) {
	        SecurityUtils.getSubject().logout();
	    }
		return "redirect:/login";
	}
	
}
