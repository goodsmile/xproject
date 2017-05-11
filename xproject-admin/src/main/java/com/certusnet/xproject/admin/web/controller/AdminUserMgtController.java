package com.certusnet.xproject.admin.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certusnet.xproject.admin.consts.AdminConstants;
import com.certusnet.xproject.admin.consts.em.AdminUserStatusEnum;
import com.certusnet.xproject.admin.consts.em.AdminUserTypeEnum;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.admin.service.AdminUserService;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;
import com.certusnet.xproject.common.support.Result;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.FileUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.BaseController;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;
/**
 * 管理后台-用户管理
 * 
 * @author 	pengpeng
 * @date   		2017年5月4日 下午3:55:23
 * @version 	1.0
 */
@RestController
public class AdminUserMgtController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AdminUserMgtController.class);
	
	@Resource(name="adminUserFacadeService")
	private AdminUserService adminUserService;
	
	@Resource(name="adminResourceFacadeService")
	private AdminResourceService adminResourceService;
	
	/**
	 * 查询用户列表
	 * @param request
	 * @param response
	 * @param userQueryForm
	 * @param orderBy
	 * @param pager
	 * @return
	 */
	@RequestMapping(value="/admin/user/list")
	public Object listRole(HttpServletRequest request, HttpServletResponse response, AdminUser userQueryForm, OrderBy orderBy, Pager pager) {
		PagingList<AdminUser> dataList = adminUserService.getUserList(userQueryForm, pager, orderBy);
		return genSuccessPagingResult(dataList);
	}
	
	/**
	 * 新增用户
	 * @param request
	 * @param response
	 * @param userAddForm
	 * @return
	 */
	@RequestMapping(value="/admin/user/add/submit", method=POST)
	public Object addUser(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminUser userAddForm) throws Exception {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		userAddForm.setCreateTime(DateTimeUtils.formatNow());
		userAddForm.setCreateBy(loginToken.getLoginId());
		userAddForm.setStatus(AdminUserStatusEnum.ADMIN_USER_STATUS_ENABLED.getStatusCode());
		userAddForm.setUserType(AdminUserTypeEnum.ADMIN_USER_TYPE_NORMAL.getTypeCode());
		
		if(StringUtils.isEmpty(userAddForm.getUserIcon())){
			userAddForm.setUserIcon(AdminConstants.DEFAULT_USER_AVATAR);
		}else{
			String userIcon = userAddForm.getUserIcon();
			if(userIcon.toLowerCase().startsWith(GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH)){
				String srcFullFileName = FileUtils.formatFilePath(request.getSession().getServletContext().getRealPath("/") + userIcon);
				userIcon = userIcon.replace(GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH, AdminConstants.USER_ICON_IMAGE_SAVE_PATH);
				String destFullFileName = FileUtils.formatFilePath(GlobalConstants.IMAGE_SERVER_ROOT_PATH + userIcon);
				FileUtils.copyFile(srcFullFileName, destFullFileName);
				userAddForm.setUserIcon(userIcon);
			}
		}
		adminUserService.createUser(userAddForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 修改用户
	 * @param request
	 * @param response
	 * @param userAddForm
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="/admin/user/edit/submit", method=POST)
	public Object editUser(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminUser userEditForm) throws Exception {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		userEditForm.setUpdateBy(loginToken.getLoginId());
		userEditForm.setUpdateTime(DateTimeUtils.formatNow());
		
		if(StringUtils.isEmpty(userEditForm.getUserIcon())){
			userEditForm.setUserIcon(AdminConstants.DEFAULT_USER_AVATAR);
		}else{
			String userIcon = userEditForm.getUserIcon();
			if(userIcon.toLowerCase().startsWith(GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH)){
				String srcFullFileName = FileUtils.formatFilePath(request.getSession().getServletContext().getRealPath("/") + userIcon);
				userIcon = userIcon.replace(GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH, AdminConstants.USER_ICON_IMAGE_SAVE_PATH);
				String destFullFileName = FileUtils.formatFilePath(GlobalConstants.IMAGE_SERVER_ROOT_PATH + userIcon);
				FileUtils.copyFile(srcFullFileName, destFullFileName);
				userEditForm.setUserIcon(userIcon);
			}
		}
		adminUserService.updateUser(userEditForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/admin/user/del")
	public Object delUser(HttpServletRequest request, HttpServletResponse response, Long id) {
		AdminUser user = new AdminUser();
		user.setUserId(id);
		adminUserService.deleteUserById(user);
		return genSuccessResult("删除成功!", null);
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @param passwdEditForm
	 * @return
	 */
	@RequestMapping(value="/admin/user/changepwd/submit", method=POST)
	public Object changeUserPasswd(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminUser passwdEditForm, Boolean forceUpdate) {
		if(forceUpdate == null){
			forceUpdate = false;
		}
		adminUserService.updatePassword(passwdEditForm, forceUpdate);
		return genSuccessResult("修改成功!", null);
	}
	
	/**
	 * 启用用户
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/admin/user/enable")
	public Object enableUser(HttpServletRequest request, HttpServletResponse response, Long userId) {
		return updateUserStatus(request, response, userId, AdminUserStatusEnum.ADMIN_USER_STATUS_ENABLED);
	}
	
	/**
	 * 禁用用户
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/admin/user/disable")
	public Object disableUser(HttpServletRequest request, HttpServletResponse response, Long userId) {
		return updateUserStatus(request, response, userId, AdminUserStatusEnum.ADMIN_USER_STATUS_DISABLED);
	}
	
	protected Result<Object> updateUserStatus(HttpServletRequest request, HttpServletResponse response, Long userId, AdminUserStatusEnum targetStatus){
		AdminUser user = new AdminUser();
		user.setUserId(userId);
		user.setStatus(targetStatus.getStatusCode());
		adminUserService.updateUserStatus(user);
		return genSuccessResult(targetStatus.getStatusName() + "成功!", null);
	}
	
	/**
	 * 获取用户拥有的角色列表
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/admin/user/roles")
	public Object loadUserRoles(HttpServletRequest request, HttpServletResponse response, Long userId, AdminRole filterParam) {
		List<AdminRole> roleList = adminUserService.getUserRoleList(userId, filterParam);
		return genSuccessResult(roleList);
	}
	
	/**
	 * 添加用户-角色配置
	 * @param request
	 * @param response
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value="/admin/user/config/add", method=POST)
	public Object addUserRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> parameter) {
		Long userId = MapUtils.getLong(parameter, "userId");
		String roleIds = MapUtils.getString(parameter, "roleIds");
		List<Long> roleIdList = new ArrayList<Long>();
		if(!StringUtils.isEmpty(roleIds)){
			String[] roleIdArray = roleIds.split(",");
			if(roleIdArray != null && roleIdArray.length > 0){
				for(String roleId : roleIdArray){
					roleIdList.add(Long.valueOf(roleId));
				}
			}
		}
		
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		AdminUser user = new AdminUser();
		user.setUserId(userId);
		adminUserService.addUserRoles(user, roleIdList, loginToken.getLoginId(), DateTimeUtils.formatNow());
		return genSuccessResult("添加成功!", null);
	}
	
	/**
	 * 删除户-角色配置
	 * @param request
	 * @param response
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value="/admin/user/config/del", method=POST)
	public Object delUserRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> parameter) {
		Long userId = MapUtils.getLong(parameter, "userId");
		String roleIds = MapUtils.getString(parameter, "roleIds");
		List<Long> roleIdList = new ArrayList<Long>();
		if(!StringUtils.isEmpty(roleIds)){
			String[] roleIdArray = roleIds.split(",");
			if(roleIdArray != null && roleIdArray.length > 0){
				for(String roleId : roleIdArray){
					roleIdList.add(Long.valueOf(roleId));
				}
			}
		}
		AdminUser user = new AdminUser();
		user.setUserId(userId);
		adminUserService.delUserRoles(user, roleIdList);
		return genSuccessResult("删除成功!", null);
	}
	
	/**
	 * 查询用户
	 * @param request
	 * @param response
	 * @param userSearchForm
	 * @param pager
	 * @param orderBy
	 * @return
	 */
	@RequestMapping("/admin/user/search")
	public Object searchUsers(HttpServletRequest request, HttpServletResponse response, AdminUser userSearchForm, Pager pager, OrderBy orderBy) {
		userSearchForm.setStatus(AdminUserStatusEnum.ADMIN_USER_STATUS_ENABLED.getStatusCode());
		PagingList<AdminUser> dataList = adminUserService.getUserList(userSearchForm, pager, orderBy);
		return genSuccessPagingResult(dataList);
	}
	
}
