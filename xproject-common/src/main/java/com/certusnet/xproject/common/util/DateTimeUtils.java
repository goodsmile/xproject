package com.certusnet.xproject.common.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.Assert;

/**
 * 基于joda-time框架的日期时间处理工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午4:45:17
 * @version  	1.0
 */
public class DateTimeUtils {

	/**
	 * 默认的日期格式: yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	
	/**
	 * 默认的日期格式: yyyy-MM-dd
	 */
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	
	/**
	 * 默认的日期+时间格式: yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_PATTERN = DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN;
	
	/**
	 * 默认带毫秒数的时间戳格式
	 */
	private static final Pattern TIMESTAMP_MSEC_REGEX_PATTERN = Pattern.compile("\\d{2}:\\d{2}:\\d{2}\\.\\d{1,3}");
	
	/**
	 * 默认不带毫秒数的时间戳格式
	 */
	private static final Pattern TIMESTAMP_REGEX_PATTERN = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
	
	/**
	 * <p>将@{code java.util.Date}转换为@{code org.joda.time.DateTime}
	 * 
	 * @param date
	 * @return
	 */
	public static DateTime from(Date date){
		Assert.notNull(date, "Parameter 'date' can not be null!");
		return new DateTime(date);
	}
	
	/**
	 * <p>将@{code java.util.Date}转换为@{code org.joda.time.DateTime}
	 * 
	 * @param date
	 * @return
	 */
	public static Date from(DateTime dateTime){
		Assert.notNull(dateTime, "Parameter 'dateTime' can not be null!");
		return dateTime.toDate();
	}
	
	/**
	 * <p>将@{code java.util.Date}以指定的日期格式格式化为字符串</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern){
		Assert.notNull(date, "Parameter 'date' can not be null!");
		Assert.hasText(pattern, "Parameter 'pattern' can not be empty!");
		return new DateTime(date).toString(pattern);
	}
	
	/**
	 * <p>以指定的日期格式格式化当前时间</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatNow(String pattern){
		Assert.hasText(pattern, "Parameter 'pattern' can not be empty!");
		return new DateTime(new Date()).toString(pattern);
	}
	
	/**
	 * <p>以默认的日期格式(yyyy-MM-dd HH:mm:ss)格式化当前时间</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatNow(){
		return new DateTime(new Date()).toString(DEFAULT_DATETIME_PATTERN);
	}
	
	/**
	 * <p>将字符串格式的日期转换为@{org.joda.time.DateTime}</p>
	 * 
	 * @param dateTimeText		- 日期字符串形式的值
	 * @param pattern			- 针对dateTimeText的日期格式
	 * @return
	 */
	public static DateTime parse2DateTime(String dateTimeText, String pattern){
		Assert.hasText(dateTimeText, "Parameter 'dateTimeText' can not be empty!");
		Assert.hasText(dateTimeText, "Parameter 'pattern' can not be empty!");
		String format = pattern;
		String text = dateTimeText;
		Matcher matcher = null;
		String suffix = ".SSS";
		//dateTimeText以毫秒结尾 && 格式pattern中没有以.SSS结尾
		if((matcher = TIMESTAMP_MSEC_REGEX_PATTERN.matcher(dateTimeText)).find() && matcher.end() == dateTimeText.length() && !pattern.endsWith(suffix)){
			format = format + suffix;
		//dateTimeText没有以毫秒结尾 && 格式pattern中以.SSS结尾
		}else if((matcher = TIMESTAMP_REGEX_PATTERN.matcher(dateTimeText)).find() && matcher.end() == dateTimeText.length() && pattern.endsWith(suffix)){
			text = text + ".0";
		}
		return DateTimeFormat.forPattern(format).parseDateTime(text);
	}
	
	/**
	 * <p>将字符串格式的日期转换为@{code java.util.Date}</p>
	 * 
	 * @param dateTimeText		- 日期字符串形式的值
	 * @param pattern			- 针对dateTimeText的日期格式
	 * @return
	 */
	public static Date parse2Date(String dateTimeText, String pattern){
		return parse2DateTime(dateTimeText, pattern).toDate();
	}
	
	/**
	 * 检测dateTimeText的日期格式是否是pattern
	 * @param dateTimeText
	 * @param pattern
	 * @return
	 */
	public static boolean checkDatePattern(String dateTimeText, String pattern) {
		if(dateTimeText != null){
			try {
				DateTime dateTime = DateTimeFormat.forPattern(pattern).parseDateTime(dateTimeText);
				return dateTime != null;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
}
