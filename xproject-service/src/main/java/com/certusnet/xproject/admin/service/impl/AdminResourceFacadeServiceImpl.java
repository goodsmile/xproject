package com.certusnet.xproject.admin.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.shiro.filter.DynamicUrlPermissionsShiroFilter;
import com.certusnet.xproject.common.web.shiro.service.ShiroCacheService;
import com.certusnet.xproject.common.web.shiro.service.UrlPermissionService;

@Lazy
@Service("adminResourceFacadeService")
public class AdminResourceFacadeServiceImpl implements AdminResourceService, UrlPermissionService, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(AdminResourceFacadeServiceImpl.class);
	
	private ApplicationContext applicationContext;
	
	@Resource(name="shiroCacheService")
	private ShiroCacheService shiroCacheService;
	
	@Resource(name="adminResourceService")
	private AdminResourceService delegate;
	
	public void createResource(AdminResource resource) {
		delegate.createResource(resource);
		new Thread(new Runnable(){
			public void run() {
				pubGlobalUrlPermissionsReload(); //系统资源发生变动,通知重新加载系统的全局(URL-权限)配置
			}
		}).start();
	}

	public void updateResource(AdminResource resource) {
		delegate.updateResource(resource);
		new Thread(new Runnable(){
			public void run() {
				pubGlobalUrlPermissionsReload(); //系统资源发生变动,通知重新加载系统的全局(URL-权限)配置
				shiroCacheService.clearAllCachedAuthorizationInfo(); //权限发生变动,需要清除所有授权缓存
			}
		}).start();
	}

	public void deleteResourceById(Long resourceId, boolean cascadeDelete) {
		delegate.deleteResourceById(resourceId, cascadeDelete);
		new Thread(new Runnable(){
			public void run() {
				pubGlobalUrlPermissionsReload(); //系统资源发生变动,通知重新加载系统的全局(URL-权限)配置
				shiroCacheService.clearAllCachedAuthorizationInfo(); //权限被删除,需要清除所有授权缓存
			}
		}).start();
	}

	public void loadTreeResources(List<AdminResource> objTreeList, List<AdminResource> resultList) {
		delegate.loadTreeResources(objTreeList, resultList);
	}

	public AdminResource getResourceById(Long resourceId) {
		return delegate.getResourceById(resourceId);
	}

	public AdminResource getThinResourceById(Long resourceId, boolean fetchInuse) {
		return delegate.getThinResourceById(resourceId, fetchInuse);
	}

	public List<AdminResource> getAllThinResourceList(boolean fetchInuse) {
		return delegate.getAllThinResourceList(fetchInuse);
	}

	public List<AdminResource> getAllResourceList(Integer actionType) {
		return delegate.getAllResourceList(actionType);
	}

	public Map<String, String> getAllUrlPermissions() {
		Map<String,String> allUrlPermissions = new LinkedHashMap<String,String>();
		List<AdminResource> allResourceList = getAllResourceList(null);
		if(!CollectionUtils.isEmpty(allResourceList)){
			for(AdminResource resource : allResourceList){
				if(!StringUtils.isEmpty(resource.getResourceUrl()) && !StringUtils.isEmpty(resource.getPermissionExpression())){
					String[] resourceUrls = resource.getResourceUrl().split(";");
					for(String resourceUrl : resourceUrls){
						resourceUrl = resourceUrl.replace("\r", "").replace("\n", "");
						allUrlPermissions.put(resourceUrl, String.format("authc, perms[%s]", resource.getPermissionExpression()));
					}
				}
			}
		}
		return allUrlPermissions;
	}
	
	public void pubGlobalUrlPermissionsReload() {
		reloadGlobalUrlPermissions(); //单机情况下，直接调用，集群情况下需要借助消息中间件(如Redis pub/sub)
	}

	public void subGlobalUrlPermissionsReload() {
		//单机情况下，nothing to do
	}

	public void reloadGlobalUrlPermissions() {
		try {
			logger.warn(">>> 重新加载系统的Shiro全局(URL-权限)配置");
			DynamicUrlPermissionsShiroFilter shiroFilter = applicationContext.getBean(DynamicUrlPermissionsShiroFilter.class);
			shiroFilter.reloadGlobalUrlPermissions();
		} catch (Exception e) {
			logger.error(String.format(">>> 重新加载系统的Shiro全局(URL-权限)配置发生异常: %s", e.getMessage()), e);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
