package com.certusnet.xproject.common.support;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;

import com.certusnet.xproject.common.consts.GlobalConstants;

import java.util.Locale;

/**
 * 全局国际化资源文件获取工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午9:28:22
 * @version  	1.0
 */
public class Messages {

    public static final Locale DEFAULT_LOCALE = GlobalConstants.SYSTEM_DEFAULT_LOCALE;

    private static AbstractMessageSource messageSource;

    public static void setMessageSource(AbstractMessageSource messageSource) {
        Messages.messageSource = messageSource;
    }
    
    public static Locale getCurrentLocale() {
    	/*Locale locale = LocaleContextHolder.getLocale();
    	if(locale == null){
    		locale = DEFAULT_LOCALE;
    	}*/
    	return DEFAULT_LOCALE;
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String message = messageSource.getMessage(code, args, defaultMessage, locale);
        Assert.hasText(message, String.format("No message (code=%s) found in i18n message resource file!", code));
        return message;
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        String message = messageSource.getMessage(code, args, defaultMessage, getCurrentLocale());
        Assert.hasText(message, String.format("No message (code=%s) found in i18n message resource file!", code));
        return message;
    }

    public static String getMessage(String code, Object[] args) {
        String message = messageSource.getMessage(code, args, "", getCurrentLocale());
        Assert.hasText(message, String.format("No message (code=%s) found in i18n message resource file!", code));
        return message;
    }

    public static String getMessage(String code, String args0) {
        String message = messageSource.getMessage(code, new Object[]{args0}, "", getCurrentLocale());
        Assert.hasText(message, String.format("No message (code=%s) found in i18n message resource file!", code));
        return message;
    }

    public static String getMessage(String code) {
        String message = messageSource.getMessage(code, null, "", getCurrentLocale());
        Assert.hasText(message, String.format("No message (code=%s) found in i18n message resource file!", code));
        return message;
    }
    
    public static MessageHolder forName(String code) {
    	return new MessageHolder(code);
    }
    
    public static MessageHolder forName(String code, Object... args) {
    	return new MessageHolder(code, args);
    }
    
    public static class MessageHolder {
    	
    	/** 国际化资源文件中的消息code */
    	private String msgCode;
    	
    	private Object[] args;
    	
    	private MessageHolder(String msgCode){
    		this.msgCode = msgCode;
    	}
    	
    	private MessageHolder(String msgCode, Object[] args){
    		this.msgCode = msgCode;
    		this.args = args;
    	}
    	
		public String getMessage(){
			return Messages.getMessage(msgCode, args);
		}
    	
    }

}
