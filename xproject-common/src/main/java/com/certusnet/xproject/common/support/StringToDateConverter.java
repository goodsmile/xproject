package com.certusnet.xproject.common.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.certusnet.xproject.common.exception.NestedRuntimeException;

/**
 * Date ==> String Type Converter
 * 
 * @author  pengpeng
 * @date 	 2015年12月23日 下午2:08:40
 * @version 1.0
 */
public class StringToDateConverter implements Converter<String, Date> {

	private String pattern = "yyyy-MM-dd HH:mm:ss";
	
	protected String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Date convert(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat(getPattern());
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			throw new NestedRuntimeException(e);
		}
	}

}
