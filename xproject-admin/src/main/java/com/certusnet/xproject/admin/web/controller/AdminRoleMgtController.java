package com.certusnet.xproject.admin.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certusnet.xproject.admin.consts.em.AdminRoleTypeEnum;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.admin.service.AdminRoleService;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.web.BaseController;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;

/**
 * 管理后台-角色管理Controller
 * 
 * @author 	pengpeng
 * @date   		2017年4月11日 下午3:35:34
 * @version 	1.0
 */
@RestController
public class AdminRoleMgtController extends BaseController {

	@Resource(name="adminResourceFacadeService")
	private AdminResourceService adminResourceService;
	
	@Resource(name="adminRoleFacadeService")
	private AdminRoleService adminRoleService;
	
	/**
	 * 查询角色列表
	 * @param request
	 * @param response
	 * @param roleQueryForm
	 * @param orderBy
	 * @param pager
	 * @return
	 */
	@RequestMapping(value="/admin/role/list")
	public Object listRole(HttpServletRequest request, HttpServletResponse response, AdminRole roleQueryForm, OrderBy orderBy, Pager pager) {
		List<AdminRole> roleList = adminRoleService.getRoleList(roleQueryForm, pager, orderBy);
		return genSuccessResult(roleList);
	}
	
	/**
	 * 新增角色
	 * @param request
	 * @param response
	 * @param roleAddForm
	 * @return
	 */
	@RequestMapping(value="/admin/role/add/submit", method=POST)
	public Object addRole(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminRole roleAddForm) {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		roleAddForm.setCreateTime(DateTimeUtils.formatNow());
		roleAddForm.setCreateBy(loginToken.getLoginId());
		roleAddForm.setRoleType(AdminRoleTypeEnum.ADMIN_ROLE_TYPE_NORMAL.getTypeCode());
		adminRoleService.createRole(roleAddForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 修改角色
	 * @param request
	 * @param response
	 * @param roleEditForm
	 * @return
	 */
	@RequestMapping(value="/admin/role/edit/submit", method=POST)
	public Object editRole(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminRole roleEditForm) {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		roleEditForm.setUpdateTime(DateTimeUtils.formatNow());
		roleEditForm.setUpdateBy(loginToken.getLoginId());
		adminRoleService.updateRole(roleEditForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/admin/role/del")
	public Object delRole(HttpServletRequest request, HttpServletResponse response, Long id) {
		adminRoleService.deleteRoleById(id);
		return genSuccessResult("删除成功!", null);
	}
	
}
