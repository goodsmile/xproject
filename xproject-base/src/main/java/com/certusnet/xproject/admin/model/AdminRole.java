package com.certusnet.xproject.admin.model;

import com.certusnet.xproject.common.support.BaseModel;
/**
 * 后台管理-角色模型
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月28日 下午6:25:07
 * @version  	1.0
 */
public class AdminRole implements BaseModel {

	private static final long serialVersionUID = 1L;

	/** 角色id */
	private Long roleId;
	
	/** 角色名称 */
	private String roleName;
	
	/** 角色代码 */
	private String roleCode;
	
	/** 角色类型#AdminRoleTypeEnum */
	private Integer roleType;
	
	/** 角色描述 */
	private String description;
	
	private String createTime;
	
	private Long createBy;
	
	private String updateTime;
	
	private Long updateBy;
	
	//以下属于辅助字段
	/**
	 * 该角色是否正在使用中
	 */
	private boolean inuse;
	
	private String roleTypeName;
	
	private String createByName;
	
	private String updateByName;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public boolean isInuse() {
		return inuse;
	}

	public void setInuse(boolean inuse) {
		this.inuse = inuse;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
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
	
}
