package com.certusnet.xproject.admin.web.controller;

import static com.certusnet.xproject.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certusnet.xproject.admin.consts.em.AdminResourceTypeEnum;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminUser;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.admin.support.AdminResourceTreeBuilder;
import com.certusnet.xproject.admin.support.AdminResourceTreeNodeConverter;
import com.certusnet.xproject.admin.web.LoginToken;
import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.AbstractXTreeBuilder;
import com.certusnet.xproject.common.support.TreeNodeConverter;
import com.certusnet.xproject.common.util.DateTimeUtils;
import com.certusnet.xproject.common.web.BaseController;
import com.certusnet.xproject.common.web.shiro.ShiroUtils;

/**
 * 管理后台-资源管理Controller
 * 
 * @author 	pengpeng
 * @date   		2017年4月11日 下午3:35:34
 * @version 	1.0
 */
@RestController
public class AdminResourceMgtController extends BaseController {

	private AbstractXTreeBuilder<Long, AdminResource> resourceTreeBuilder = new AdminResourceTreeBuilder();
	
	private TreeNodeConverter<AdminResource,Map<String,Object>> resourceTreeNodeConverter = new AdminResourceTreeNodeConverter();
	
	@Resource(name="adminResourceFacadeService")
	private AdminResourceService adminResourceService;
	
	/**
	 * 获取可用的资源树结构
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/admin/resource/available", method=GET, produces=APPLICATION_JSON)
	public Object getAvailableResources(HttpServletRequest request, HttpServletResponse response) {
		List<AdminResource> allResourceList = adminResourceService.getAllResourceList(null);
		List<Map<String,Object>> dataList = resourceTreeBuilder.buildObjectTree(GlobalConstants.DEFAULT_ADMIN_ROOT_RESOURCE_ID, allResourceList, resourceTreeNodeConverter);
		return genSuccessResult(dataList);
	}
	
	/**
	 * 新增资源
	 * @param request
	 * @param response
	 * @param resourceAddForm
	 * @return
	 */
	@RequestMapping(value="/admin/resource/add/submit", method=POST, consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Object addResource(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminResource resourceAddForm) {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		resourceAddForm.setCreateTime(DateTimeUtils.formatNow());
		resourceAddForm.setCreateBy(loginToken.getLoginId());
		resourceAddForm.setResourceType(AdminResourceTypeEnum.ADMIN_RESOURCE_TYPE_NORMAL.getTypeCode());
		adminResourceService.createResource(resourceAddForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 修改资源
	 * @param request
	 * @param response
	 * @param resourceEditForm
	 * @return
	 */
	@RequestMapping(value="/admin/resource/edit/submit", method=POST, consumes=APPLICATION_JSON, produces=APPLICATION_JSON)
	public Object editResource(HttpServletRequest request, HttpServletResponse response, @RequestBody AdminResource resourceEditForm) {
		LoginToken<AdminUser> loginToken = ShiroUtils.getSessionAttribute(LoginToken.LOGIN_TOKEN_SESSION_KEY);
		resourceEditForm.setUpdateTime(DateTimeUtils.formatNow());
		resourceEditForm.setUpdateBy(loginToken.getLoginId());
		adminResourceService.updateResource(resourceEditForm);
		return genSuccessResult("保存成功!", null);
	}
	
	/**
	 * 删除资源
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/admin/resource/del", method=GET, produces=APPLICATION_JSON)
	public Object delResource(HttpServletRequest request, HttpServletResponse response, Long id) {
		adminResourceService.deleteResourceById(id, true);
		return genSuccessResult("删除成功!", null);
	}
	
}
