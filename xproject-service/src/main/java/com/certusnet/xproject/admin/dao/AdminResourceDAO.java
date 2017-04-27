package com.certusnet.xproject.admin.dao;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;

/**
 * 系统资源DAO
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午12:59:43
 * @version  	1.0
 */
public interface AdminResourceDAO {

	/**
	 * 创建资源
	 * @param resource
	 */
	public void insertResource(AdminResource resource);
	
	/**
	 * 修改资源
	 * @param resource
	 */
	public void updateResource(AdminResource resource);
	
	/**
	 * 根据资源id删除资源
	 * @param resourceId
	 */
	public void deleteResourceByIds(Long... resourceIds);
	
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
	 * 获取所有资源列表
	 * @param actionType - 功能类型,为null则查询所有
	 * @return
	 */
	public List<AdminResource> getAllResourceList(Integer actionType);
	
	/**
	 * 获取所有资源列表(资源id,名称)
	 * @param fetchInuse - 关联查询出inuse状态
	 * @return
	 */
	public List<AdminResource> getAllThinResourceList(boolean fetchInuse);
	
}
