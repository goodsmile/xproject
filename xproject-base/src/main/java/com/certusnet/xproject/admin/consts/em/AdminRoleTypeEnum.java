package com.certusnet.xproject.admin.consts.em;

/**
 * 角色类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum AdminRoleTypeEnum {
	
	ADMIN_ROLE_TYPE_SYSTEM(0, "系统角色"), ADMIN_ROLE_TYPE_NORMAL(1, "普通角色");
	
	private Integer typeCode;
	
	private String typeName;

	private AdminRoleTypeEnum(Integer typeCode, String typeName) {
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
	
	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public static AdminRoleTypeEnum getType(Integer typeCode) {
		for(AdminRoleTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
