package com.certusnet.xproject.admin.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certusnet.xproject.admin.consts.em.AdminRoleTypeEnum;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.admin.service.AdminRoleService;
import com.certusnet.xproject.admin.support.AdminResourceSimpleTreeNodeConverter;
import com.certusnet.xproject.admin.support.AdminResourceTreeBuilder;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.AbstractXTreeBuilder;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;
import com.certusnet.xproject.common.support.TreeNodeConverter;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.util.StringUtils;
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

	private AbstractXTreeBuilder<Long, AdminResource> resourceTreeBuilder = new AdminResourceTreeBuilder();
	
	private TreeNodeConverter<AdminResource,Map<String,Object>> resourceTreeNodeConverter = new AdminResourceSimpleTreeNodeConverter();
	
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
		PagingList<AdminRole> roleList = adminRoleService.getRoleList(roleQueryForm, pager, orderBy);
		return genSuccessPagingResult(roleList);
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
	
	/**
	 * 加载角色-资源配置关系
	 * @param request
	 * @param response
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/role/resources")
	public Object loadRoleResources(HttpServletRequest request, HttpServletResponse response, Long roleId) throws Exception {
		List<AdminResource> allResourceList = adminResourceService.getAllResourceList(null);
		List<AdminResource> roleResourceList = adminRoleService.getResourceListByRoleId(roleId);
		List<Long> checkedResourceIdList = new ArrayList<Long>();
		if(!CollectionUtils.isEmpty(roleResourceList)){
			for(AdminResource roleResource : roleResourceList){
				checkedResourceIdList.add(roleResource.getResourceId());
			}
		}
		List<Map<String,Object>> dataList = resourceTreeBuilder.buildObjectTree(GlobalConstants.DEFAULT_ADMIN_ROOT_RESOURCE_ID, allResourceList, resourceTreeNodeConverter);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("allResourceList", dataList);
		dataMap.put("checkedResourceIdList", checkedResourceIdList);
		return genSuccessResult(dataMap);
	}
	
	/**
	 * 配置角色-资源关系
	 * @param request
	 * @param response
	 * @param parameter
	 * @return
	 */
	@RequestMapping(value="/admin/role/config/submit", method=POST)
	public Object configRoleResources(HttpServletRequest request, HttpServletResponse response,  @RequestBody Map<String,Object> parameter) {
		List<Long> resourceIdList = new ArrayList<Long>();
		String resourceIds = MapUtils.getString(parameter, "resourceIds");
		Long roleId = MapUtils.getLong(parameter, "roleId");
		if(!StringUtils.isEmpty(resourceIds)){
			String[] resourceIdArray = resourceIds.split(",");
			if(resourceIdArray != null && resourceIdArray.length > 0){
				for(String resourceId : resourceIdArray){
					resourceIdList.add(Long.valueOf(resourceId));
				}
			}
		}
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		adminRoleService.configRoleResources(roleId, resourceIdList, loginToken.getLoginId(), DateTimeUtils.formatNow());
		return genSuccessResult("配置成功!", null);
	}
	
}
