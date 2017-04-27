package com.certusnet.xproject.common.web.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 防重复提交token注解
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月26日 下午4:31:15
 * @version  	1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Token {

	TokenAction action();
	
	String[] key() default {"token"};
	
}
