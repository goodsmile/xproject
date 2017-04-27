package com.certusnet.xproject.admin.web;
/**
 * 用户登录令牌
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2015年12月17日 上午10:10:52
 * @version  	1.0
 */
public class LoginToken<T> {

	/**
	 * 登录令牌在会话中的key
	 */
	public static final String LOGIN_TOKEN_SESSION_KEY = "loginToken";
	
	private Long loginId;
	
	private String loginName;
	
	private String lastLoginTime;
	
	private String loginTime;
	
	private Integer loginTimes;
	
	private String loginAddrIp;
	
	private String loginUserIcon;
	
	private String loginRoleNames;
	
	private T loginUser;

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getLoginAddrIp() {
		return loginAddrIp;
	}

	public void setLoginAddrIp(String loginAddrIp) {
		this.loginAddrIp = loginAddrIp;
	}

	public String getLoginUserIcon() {
		return loginUserIcon;
	}

	public void setLoginUserIcon(String loginUserIcon) {
		this.loginUserIcon = loginUserIcon;
	}

	public String getLoginRoleNames() {
		return loginRoleNames;
	}

	public void setLoginRoleNames(String loginRoleNames) {
		this.loginRoleNames = loginRoleNames;
	}

	public T getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(T loginUser) {
		this.loginUser = loginUser;
	}

}
