package com.certusnet.xproject.common.web.springmvc.interceptor;
/**
 * 防重复提交token的动作
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月26日 下午4:14:22
 * @version  	1.0
 */
public enum TokenAction {
	
	/**
	 * 在去下个页面之前创建Token并放入session中
	 */
	CREATE, 
	
	/**
	 * 提交页面的token校验及删除
	 */
	CHECK
	
}
