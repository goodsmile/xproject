package com.certusnet.xproject.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.consts.em.AdminResourceActionTypeEnum;
import com.certusnet.xproject.admin.consts.em.AdminResourceTypeEnum;
import com.certusnet.xproject.admin.mapper.AdminResourceMapper;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.service.AdminResourceService;
import com.certusnet.xproject.common.support.BusinessAssert;
import com.certusnet.xproject.common.support.ValidationAssert;
import com.certusnet.xproject.common.util.CollectionUtils;
import com.certusnet.xproject.common.util.ModelDecodeUtils;
import com.certusnet.xproject.common.util.StringUtils;

@Service("adminResourceService")
public class AdminResourceServiceImpl implements AdminResourceService {

	public static final Consumer<AdminResource> ADMIN_RESOURCE_DECODER = model -> {
		if(model.getResourceType() != null){
			AdminResourceTypeEnum em = AdminResourceTypeEnum.getType(model.getResourceType());
			if(em != null){
				model.setResourceTypeName(em.getTypeName());
			}
		}
		if(model.getActionType() != null){
			AdminResourceActionTypeEnum em = AdminResourceActionTypeEnum.getType(model.getActionType());
			if(em != null){
				model.setActionTypeName(em.getTypeName());
			}
		}
	};
	
	@Autowired
	private AdminResourceMapper adminResourceMapper;

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void createResource(AdminResource resource) {
		ValidationAssert.notNull(resource, "参数不能为空!");
		try {
			resource.setPermissionExpression(StringUtils.defaultIfEmpty(resource.getPermissionExpression(), null));
			resource.setResourceUrl(StringUtils.defaultIfEmpty(resource.getResourceUrl(), null));
			adminResourceMapper.insertResource(resource);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("RESOURCE_NAME"), "新增资源失败,该资源名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("PERMISSION_EXPRESSION"), "新增资源失败,该权限表达式已经存在!");
			throw e;
		}
	}

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void updateResource(AdminResource resource) {
		ValidationAssert.notNull(resource, "参数不能为空!");
		ValidationAssert.notNull(resource.getResourceId(), "资源id不能为空!");
		resource.setPermissionExpression(StringUtils.defaultIfEmpty(resource.getPermissionExpression(), null));
		resource.setResourceUrl(StringUtils.defaultIfEmpty(resource.getResourceUrl(), null));
		AdminResource presource = adminResourceMapper.selectThinResourceById(resource.getResourceId(), true);
		ValidationAssert.notNull(presource, "该资源已经不存在了!");
		try {
			adminResourceMapper.updateResource(resource);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("RESOURCE_NAME"), "修改资源失败,该资源名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("PERMISSION_EXPRESSION"), "修改资源失败,该权限表达式已经存在!");
			throw e;
		}
	}

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void deleteResourceById(Long resourceId, boolean cascadeDelete) {
		ValidationAssert.notNull(resourceId, "资源id不能为空!");
		AdminResource delResource = adminResourceMapper.selectThinResourceById(resourceId, true);
		ValidationAssert.notNull(delResource, "该资源已经不存在了!");
		BusinessAssert.isTrue(!AdminResourceTypeEnum.ADMIN_RESOURCE_TYPE_SYSTEM.getTypeCode().equals(delResource.getResourceType()), "删除资源失败,系统资源不允许删除!");
		BusinessAssert.isTrue(!delResource.isInuse(), String.format("删除资源失败,资源[%s]已经在使用不允许删除!", delResource.getResourceName()));
		if(cascadeDelete){
			List<AdminResource> allThinResourceList = adminResourceMapper.selectAllThinResourceList(true);
			if(!CollectionUtils.isEmpty(allThinResourceList)){
				List<AdminResource> childResourceList = new ArrayList<AdminResource>();
				for(AdminResource resource : allThinResourceList){
					if(resource.getResourceId().equals(resourceId)){
						childResourceList.add(resource);
					}
				}
				loadChildResources(allThinResourceList, resourceId, childResourceList);
				for(AdminResource resource : childResourceList){
					BusinessAssert.isTrue(!AdminResourceTypeEnum.ADMIN_RESOURCE_TYPE_SYSTEM.getTypeCode().equals(resource.getResourceType()), "删除资源失败,其子资源中存在系统资源不允许删除!");
					BusinessAssert.isTrue(!resource.isInuse(), String.format("删除资源失败,其子资源[%s]已经在使用不允许删除!", resource.getResourceName()));
				}
				for(int i = 0, len = childResourceList.size(); i < len; i++){
					adminResourceMapper.deleteResourceById(childResourceList.get(i).getResourceId());
				}
			}
		}else{
			adminResourceMapper.deleteResourceById(resourceId);
		}
	}
	
	private void loadChildResources(List<AdminResource> allResourceList, Long parentResourceId, List<AdminResource> childResourceList){
		if(!CollectionUtils.isEmpty(allResourceList)){
			for(AdminResource resource : allResourceList){
				if(resource.getParentResourceId().equals(parentResourceId)){
					childResourceList.add(resource);
					loadChildResources(allResourceList, resource.getResourceId(), childResourceList);
				}
			}
		}
	}
	
	public AdminResource getResourceById(Long resourceId) {
		return ModelDecodeUtils.decodeModel(adminResourceMapper.selectResourceById(resourceId), ADMIN_RESOURCE_DECODER);
	}

	public AdminResource getThinResourceById(Long resourceId, boolean fetchInuse) {
		return adminResourceMapper.selectThinResourceById(resourceId, fetchInuse);
	}

	public List<AdminResource> getAllThinResourceList(boolean fetchInuse) {
		return adminResourceMapper.selectAllThinResourceList(fetchInuse);
	}

	public List<AdminResource> getAllResourceList(Integer actionType) {
		return ModelDecodeUtils.decodeModel(adminResourceMapper.selectAllResourceList(actionType), ADMIN_RESOURCE_DECODER);
	}
	
}