package com.certusnet.xproject.admin.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.certusnet.xproject.admin.consts.em.AdminResourceActionTypeEnum;
import com.certusnet.xproject.admin.consts.em.AdminResourceTypeEnum;
import com.certusnet.xproject.admin.dao.AdminResourceDAO;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.common.mybatis.DefaultBaseMybatisDAO;
import com.certusnet.xproject.common.mybatis.ModelHandler;

@Repository("adminResourceDAO")
public class AdminResourceDAOImpl extends DefaultBaseMybatisDAO implements AdminResourceDAO {

	public void insertResource(AdminResource resource) {
		this.getSqlSessionTemplate().insert(getMapperKey("insertResource"), resource);
	}

	public void updateResource(AdminResource resource) {
		this.getSqlSessionTemplate().update(getMapperKey("updateResource"), resource);
	}

	public void deleteResourceByIds(Long... resourceIds) {
		this.getSqlSessionTemplate().batchDelete(getMapperKey("deleteResourceById"), Arrays.asList(resourceIds));
	}

	public AdminResource getResourceById(Long resourceId) {
		return this.getSqlSessionTemplate().selectOne(getMapperKey("selectResourceById"), resourceId, new AdminResourceModelHandler());
	}
	
	public AdminResource getThinResourceById(Long resourceId, boolean fetchInuse) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("resourceId", resourceId);
		paramMap.put("fetchInuse", fetchInuse);
		return this.getSqlSessionTemplate().selectOne(getMapperKey("selectThinResourceById"), paramMap);
	}

	public List<AdminResource> getAllResourceList(Integer actionType) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("actionType", actionType);
		return this.getSqlSessionTemplate().selectList(getMapperKey("selectAllResourceList"), paramMap, new AdminResourceModelHandler());
	}
	
	public List<AdminResource> getAllThinResourceList(boolean fetchInuse) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("fetchInuse", fetchInuse);
		return this.getSqlSessionTemplate().selectList(getMapperKey("selectAllThinResourceList"), paramMap);
	}

	protected Class<?> getBoundModelClass() {
		return AdminResource.class;
	}
	
	public static class AdminResourceModelHandler implements ModelHandler<AdminResource> {

		public void handleModel(AdminResource element) {
			if(element.getResourceType() != null){
				AdminResourceTypeEnum em = AdminResourceTypeEnum.getType(element.getResourceType());
				if(em != null){
					element.setResourceTypeName(em.getTypeName());
				}
			}
			if(element.getActionType() != null){
				AdminResourceActionTypeEnum em = AdminResourceActionTypeEnum.getType(element.getActionType());
				if(em != null){
					element.setActionTypeName(em.getTypeName());
				}
			}
		}
		
	}

}