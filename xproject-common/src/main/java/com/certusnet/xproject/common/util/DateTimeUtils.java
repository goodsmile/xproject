package com.certusnet.xproject.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
	
	private static ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
	
	/**
	 * java.util.Date转java.time.LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime toDateTime(Date date){
		Assert.notNull(date, "Parameter 'date' can not be null!");
		return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
	}
	
	/**
	 * java.time.LocalDateTime转java.util.Date
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date toDate(LocalDateTime dateTime){
		Assert.notNull(dateTime, "Parameter 'dateTime' can not be null!");
		return Date.from(dateTime.atZone(DEFAULT_ZONE).toInstant());
	}
	
	/**
	 * <p>将@{code java.time.LocalDateTime}以指定的日期格式格式化为字符串</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(LocalDateTime dateTime, String pattern){
		Assert.notNull(dateTime, "Parameter 'dateTime' can not be null!");
		Assert.hasText(pattern, "Parameter 'pattern' can not be empty!");
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
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
		return format(toDateTime(date), pattern);
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
		return format(LocalDateTime.now(), pattern);
	}
	
	/**
	 * <p>以默认的日期格式(yyyy-MM-dd HH:mm:ss)格式化当前时间</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatNow(){
		return formatNow(DEFAULT_DATETIME_PATTERN);
	}
	
	/**
	 * <p>将字符串格式的日期转换为@{code java.util.Date}</p>
	 * 
	 * @param dateTimeText		- 日期字符串形式的值
	 * @param pattern			- 针对dateTimeText的日期格式
	 * @return
	 */
	public static LocalDateTime parse(String dateTimeText, String pattern){
		Assert.hasText(dateTimeText, "Parameter 'dateTimeText' can not be empty!");
		Assert.hasText(dateTimeText, "Parameter 'pattern' can not be empty!");
		return LocalDateTime.parse(dateTimeText, DateTimeFormatter.ofPattern(pattern));
	}
	
	public static void main(String[] args) {
		System.out.println(DEFAULT_ZONE);
		Date date = new Date();
		System.out.println(date);
		System.out.println(format(date, DEFAULT_DATETIME_PATTERN));
	}
	
}
