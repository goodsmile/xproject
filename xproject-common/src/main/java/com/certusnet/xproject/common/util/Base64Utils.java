package com.certusnet.xproject.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * 基于Base64的加解密工具类
 * 
 * @author  pengpeng
 * @date 	 2015年4月24日 下午6:26:07
 * @version 1.0
 */
public class Base64Utils {
	
	/**
	 * 使用Base64对数据进行加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encode(String plainText) {
		if(plainText == null){
			return null;
		}
		return encode(plainText.getBytes());
	}
	
	/**
	 * 使用Base64对数据进行加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encode(byte[] bytes) {
		Base64 base64 = new Base64();
		bytes = base64.encode(bytes);
		String s = new String(bytes);
		return s;
	}
	
	/**
	 * 使用Base64对已加密数据进行解密
	 * 
	 * @param encodedText
	 * @return
	 */
	public static String decode(String encodedText) {
		if(encodedText == null){
			return null;
		}
		return decode(encodedText.getBytes());
	}
	
	/**
	 * 使用Base64对已加密数据进行解密
	 * 
	 * @param encodedText
	 * @return
	 */
	public static String decode(byte[] bytes) {
		Base64 base64 = new Base64();
		bytes = base64.decode(bytes);
		String s = new String(bytes);
		return s;
	}
	
}
