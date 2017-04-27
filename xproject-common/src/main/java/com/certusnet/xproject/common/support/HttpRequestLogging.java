package com.certusnet.xproject.common.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 对于打在Spring MVC控制器或其方法上该注解将进行日志记录
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午9:17:42
 * @version  	1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HttpRequestLogging {

	String title();
	
	boolean persistIntoDatabase() default true;
	
}
