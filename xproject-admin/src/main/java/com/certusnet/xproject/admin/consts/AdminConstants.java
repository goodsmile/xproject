package com.certusnet.xproject.admin.consts;

import com.certusnet.xproject.common.consts.AbstractConstants;

/**
 * 后台管理常量
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月15日 下午1:38:38
 * @version  	1.0
 */
public class AdminConstants extends AbstractConstants {

	/**
	 * 用户默认头像
	 */
	public static final String DEFAULT_USER_ICON = valueOf("/resources/images/default-user-icon.png");
	
	/**
	 * 用户头像默认保存路径,相对于#GlobalConstants.IMAGE_SERVER_ROOT
	 */
	public static final String USER_ICON_IMAGE_SAVE_PATH = valueOf("/img/admin/usericon/");
	
}
