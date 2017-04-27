package com.certusnet.xproject.admin.consts.em;
/**
 * 资源功能类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum AdminResourceActionTypeEnum {
	
	ADMIN_RESOURCE_ACTION_TYPE_MENU(0, "菜单"), ADMIN_RESOURCE_ACTION_TYPE_BUTTON(1, "按钮");
	
	private Integer typeCode;
	
	private String typeName;

	private AdminResourceActionTypeEnum(Integer typeCode, String typeName) {
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

	public static AdminResourceActionTypeEnum getType(Integer typeCode) {
		for(AdminResourceActionTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
