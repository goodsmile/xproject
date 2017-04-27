package com.certusnet.xproject.admin.consts.em;

/**
 * enable/disable状态枚举
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum AdminUserStatusEnum {
	
	ADMIN_USER_STATUS_ENABLED(1, "启用"), ADMIN_USER_STATUS_DISABLED(0, "禁用");
	
	private Integer statusCode;
	
	private String statusName;

	private AdminUserStatusEnum(Integer statusCode, String statusName) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public static AdminUserStatusEnum getStatus(Integer statusCode) {
		for(AdminUserStatusEnum em : values()){
			if(em.getStatusCode().equals(statusCode)){
				return em;
			}
		}
		return null;
	}
	
}
