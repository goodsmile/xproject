package com.certusnet.xproject.common.support;

import java.lang.annotation.*;

/**
 * 对于打上该注解的final字段或类中的final字段都将作为ServletContext的attribute
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午2:50:50
 * @version  	1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ServletContextAttribute {

}
