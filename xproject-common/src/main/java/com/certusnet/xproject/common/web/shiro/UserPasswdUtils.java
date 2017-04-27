package com.certusnet.xproject.common.web.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 用户密码工具类
 * 
 * @author  pengpeng
 * @date 	 2015年4月5日 下午10:37:20
 * @version 1.0
 */
public class UserPasswdUtils {

	public static String encryptPassword(String password, String salt) {
		SimpleHash hash = new SimpleHash("md5", password, salt, 1);
		String encodedPassword = hash.toHex();
		return encodedPassword;
	}
	
	public static String encryptPassword(String password, String salt, int hashIterations) {
		SimpleHash hash = new SimpleHash("md5", password, salt, hashIterations);
		String encodedPassword = hash.toHex();
		return encodedPassword;
	}
	
}
