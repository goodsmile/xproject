package com.certusnet.xproject.admin.service;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;

/**
 * 系统资源Service
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午12:55:56
 * @version  	1.0
 */
public interface AdminResourceService {

	/**
	 * 创建资源
	 * @param resource
	 */
	public void createResource(AdminResource resource);
	
	/**
	 * 修改资源
	 * @param resource
	 */
	public void updateResource(AdminResource resource);
	
	/**
	 * 根据资源id删除资源
	 * @param resourceId
	 * @param cascadeDelete - 是否级联删除子节点
	 */
	public void deleteResourceById(Long resourceId, boolean cascadeDelete);
	
	/**
	 * 通过递归处理treeList然后将结果
	 * @param objTreeList		- 对象树结构的资源列表
	 * @param resultList		- 一个新的空的结果列表,用于接收结果
	 */
	public void loadTreeResources(List<AdminResource> objTreeList, List<AdminResource> resultList);
	
	/**
	 * 根据资源id获取资源详情
	 * @param resourceId
	 */
	public AdminResource getResourceById(Long resourceId);
	
	/**
	 * 根据资源id获取资源信息(资源id,名称)
	 * @param resourceId
	 * @param fetchInuse - 关联查询出inuse状态
	 */
	public AdminResource getThinResourceById(Long resourceId, boolean fetchInuse);
	
	/**
	 * 获取所有资源列表(轻量级Resource对象)
	 * @param fetchInuse - 关联查询出inuse状态
	 * @return
	 */
	public List<AdminResource> getAllThinResourceList(boolean fetchInuse);
	
	/**
	 * 获取所有资源列表
	 * @param actionType - 功能类型,为null则查询所有
	 * @return
	 */
	public List<AdminResource> getAllResourceList(Integer actionType);
	
}
