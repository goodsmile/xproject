package com.certusnet.xproject.common.web.shiro.filter;

import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.certusnet.xproject.common.util.ReflectionUtils;
/**
 * 非单例的ShiroFilter FactoryBean
 * 
 * @author  pengpeng
 * @date 	 2016年3月1日 下午6:26:25
 * @version 1.0
 */
public class PrototypedShiroFilterFactoryBean extends ShiroFilterFactoryBean implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(PrototypedShiroFilterFactoryBean.class);
	
	private ApplicationContext applicationContext;
	
	public boolean isSingleton() {
		return false;
	}
	
	public Object getObject() throws Exception {
		logger.info(">>> create filter bean : {}, singleton : {}", getObjectType(), isSingleton());
		ReflectionUtils.setFieldValue(ReflectionUtils.findField(ShiroFilterFactoryBean.class, "instance"), this, null);
		Map<String, String> filterChainDefinitionMap = (Map<String, String>) applicationContext.getBean(Ini.Section.class);
		setFilterChainDefinitionMap(filterChainDefinitionMap);
		return super.getObject();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
