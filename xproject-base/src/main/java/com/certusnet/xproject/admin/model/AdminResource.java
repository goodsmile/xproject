package com.certusnet.xproject.admin.model;

import java.util.List;

import com.certusnet.xproject.common.support.BaseModel;
/**
 * 后台管理-资源模型
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月28日 下午6:27:46
 * @version  	1.0
 */
public class AdminResource implements BaseModel, Comparable<AdminResource> {

	private static final long serialVersionUID = 1L;
	
	/** 资源id */
	private Long resourceId;
	
	/** 资源名称 */
	private String resourceName;
	
	/** 父资源id */
	private Long parentResourceId;
	
	/** 父资源名称*/
	private String parentResourceName;
	
	/** 权限表达式 */
	private String permissionExpression;
	
	/** 功能类型：#AdminResourceActionTypeEnum */
	private Integer actionType;
	
	/** 资源类型#AdminResourceTypeEnum */
	private Integer resourceType;
	
	/** 资源URL */
	private String resourceUrl;
	
	/** 资源ICON */
	private String resourceIcon;
	
	/** 排序号 */
	private Integer siblingsIndex;
	
	private String createTime;
	
	private Long createBy;
	
	private String updateTime;
	
	private Long updateBy;
	
	//以下属于辅助字段
	
	private Integer resourceLevel;
	
	private String treeNodePath;
	
	private List<AdminResource> subResourceList;
	
	private boolean inuse;
	
	private String resourceTypeName;
	
	private String actionTypeName;
	
	private String createByName;
	
	private String updateByName;
	
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Long getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(Long parentResourceId) {
		this.parentResourceId = parentResourceId;
	}

	public String getParentResourceName() {
		return parentResourceName;
	}

	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
	}

	public String getPermissionExpression() {
		return permissionExpression;
	}

	public void setPermissionExpression(String permissionExpression) {
		this.permissionExpression = permissionExpression;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public Integer getResourceType() {
		return resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getResourceIcon() {
		return resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public Integer getSiblingsIndex() {
		return siblingsIndex;
	}

	public void setSiblingsIndex(Integer siblingsIndex) {
		this.siblingsIndex = siblingsIndex;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(Integer resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getTreeNodePath() {
		return treeNodePath;
	}

	public void setTreeNodePath(String treeNodePath) {
		this.treeNodePath = treeNodePath;
	}

	public List<AdminResource> getSubResourceList() {
		return subResourceList;
	}

	public void setSubResourceList(List<AdminResource> subResourceList) {
		this.subResourceList = subResourceList;
	}

	public boolean isInuse() {
		return inuse;
	}

	public void setInuse(boolean inuse) {
		this.inuse = inuse;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getActionTypeName() {
		return actionTypeName;
	}

	public void setActionTypeName(String actionTypeName) {
		this.actionTypeName = actionTypeName;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public int compareTo(AdminResource o) {
		if(o == null){
			return -1;
		}
		if(this.siblingsIndex == null){
			return 1;
		}
		if(o.siblingsIndex == null){
			return -1;
		}
		return this.siblingsIndex - o.siblingsIndex;
	}
	
}
