package com.certusnet.xproject.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.common.support.DefaultDatabase;

/**
 * 系统资源DAO
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午2:37:10
 * @version 	1.0
 */
@DefaultDatabase
public interface AdminResourceMapper {

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
	public void deleteResourceById(Long resourceId);
	
	/**
	 * 根据资源id获取资源详情
	 * @param resourceId
	 */
	public AdminResource selectResourceById(Long resourceId);
	
	/**
	 * 根据资源id获取资源信息(资源id,名称)
	 * @param resourceId
	 * @param fetchInuse - 关联查询出inuse状态
	 */
	public AdminResource selectThinResourceById(@Param("resourceId")Long resourceId, @Param("fetchInuse") boolean fetchInuse);
	
	/**
	 * 获取所有资源列表
	 * @param actionType - 功能类型,为null则查询所有
	 * @return
	 */
	public List<AdminResource> selectAllResourceList(Integer actionType);
	
	/**
	 * 获取所有资源列表(资源id,名称)
	 * @param fetchInuse - 关联查询出inuse状态
	 * @return
	 */
	public List<AdminResource> selectAllThinResourceList(boolean fetchInuse);
	
}
