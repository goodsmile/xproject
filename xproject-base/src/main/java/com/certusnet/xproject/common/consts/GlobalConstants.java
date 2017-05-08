package com.certusnet.xproject.common.consts;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.certusnet.xproject.common.support.ConstValue;

/**
 * 全局共通常量
 * 
 * @author  pengpeng
 * @date 	 2015年4月18日 下午5:50:07
 * @version 1.0
 */
public class GlobalConstants extends AbstractConstants {

	/**
	 * 系统默认字符编码
	 */
	public static final String SYSTEM_DEFAULT_ENCODING = valueOf("UTF-8");
	
	/**
	 * 系统默认Locale
	 */
	public static final Locale SYSTEM_DEFAULT_LOCALE = valueOf(new Locale("zh", "CN"));
	
	/**
	 * 图片服务器的物理根目录,如 /data/img
	 */
	@ConstValue("${global.imgserver.root}")
	public static final String IMAGE_SERVER_ROOT_PATH = valueOf("/data/xproject");
	
	/**
	 * 图片服务器的物理根目录,如 http://127.0.0.1/img
	 */
	@ConstValue("${global.imgserver.domain}")
	public static final String IMAGE_SERVER_DOMAIN = valueOf("http://127.0.0.1");
	
	/**
	 * 系统默认的上传文件临时存储路径
	 */
	public static final String DEFAULT_UPLOAD_SAVE_PATH = valueOf("/upload/temp/");
	
	/**
	 * 系统默认的允许上传图片格式
	 */
	public static final List<String> DEFAULT_UPLOAD_IMAGE_FORMATS = valueOf(Arrays.asList("jpg", "jpeg", "png"));
	
	/**
	 * 返回结果之成功
	 */
	public static final String RESULT_CODE_SUCCESS = valueOf("1");

	/**
	 * 返回结果之失败
	 */
	public static final String RESULT_CODE_FAILURE = valueOf("0");

	/**
	 * 针对数据库字段,诸如:'是','真','已删除',...等等由数字"1"代表的真值
	 */
	public static final String DEFAULT_YES_TRUE_FLAG = valueOf("1");

	/**
	 * 针对数据库字段,诸如:'否','假','未删除',...等等由数字"0"代表的假值
	 */
	public static final String DEFAULT_NO_FALSE_FLAG = valueOf("0");
	
	/**
	 * 后台管理-资源菜单默认的根菜单id
	 */
	public static final Long DEFAULT_ADMIN_ROOT_RESOURCE_ID = 0L;
	
	/**
	 * 默认的密码加密次数
	 */
	public static final int DEFAULT_PASSWORD_HASH_ITERATIONS = 1;
	
}
