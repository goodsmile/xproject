package com.certusnet.xproject.common.support;

import java.lang.annotation.*;

/**
 * 类似于org.springframework.beans.factory.annotation.Value注解
 * 专门用于给public/private static final修饰的常量从properties属性配置文件中注入值的
 * 例如：
 * 
 * public class MyConstants {
 * 		
 * 		@ConstValue("${global.imgserver.domain}")
 * 		public static final String GLOBAL_IMGSERVER_DOMAIN = valueOf('The default image server domain url');
 * 
 * }
 * 
 * global.properties中配置如下:
 * 
 * #全局图片服务器domain
 * global.imgserver.domain=http://img.xxx.com
 * 
 * Spring配置如下:
 * <!-- 全局properties属性源 -->
 * <bean id="globalPropertySources" class="com.certusnet.xproject.common.support.GlobalPropertySources">
 *		<property name="ignoreResourceNotFound" value="true"/>
 *		<property name="fileEncoding" value="UTF-8"/>
 *		<property name="locations">
 *			<list>
 *				<value>classpath:jdbc.properties</value>
 *				<value>classpath:dubbo.properties</value>
 *				<value>classpath:global.properties</value>
 *				<value>classpath:redis.properties</value>
 *				<value>classpath:url.properties</value>
 *			</list>
 *		</property>
 *	</bean>
 *	
 *	<!-- 
 *		一个全局工具,用于解析占位符形式的属性值,
 *		globalPropertyResolver.resolvePlaceholders("${img.domain}${img.domain.sub:defaultIfNotFound}") -> "http://www.xxx.com/img/defaultIfNotFound"
 *		globalPropertyResolver.getProperty("img.domain")			-> "http://www.xxx.com/img"
 *	 -->
 *	<bean id="globalPropertyResolver" class="org.springframework.core.env.PropertySourcesPropertyResolver">
 *		<constructor-arg index="0" ref="globalPropertySources"/>
 *		<property name="conversionService" ref="conversionService"/>
 *	</bean>
 * 
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午2:50:50
 * @version  	1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ConstValue {

	/**
	 * The actual value expression: e.g. "${global.imgserver.domain}".
	 */
	String value();
	
}
