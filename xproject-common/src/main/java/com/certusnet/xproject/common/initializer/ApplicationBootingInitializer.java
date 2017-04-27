package com.certusnet.xproject.common.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.web.context.WebApplicationContext;

import com.certusnet.xproject.common.consts.ApplicationConstants;
import com.certusnet.xproject.common.support.Messages;
import com.certusnet.xproject.common.util.SpringUtils;

/**
 * 应用中不依赖于Servlet环境的系统常量初始化
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月3日 上午10:48:02
 * @version  	1.0
 */
public class ApplicationBootingInitializer extends AbstractApplicationInitializer {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationBootingInitializer.class);

	public void initialize(ApplicationContext applicationContext) throws Exception {
		logger.info(">>> 初始化应用中不依赖于Servlet环境的系统常量!");
		ApplicationContext rootApplicationContext = applicationContext;
    	if(applicationContext instanceof WebApplicationContext && applicationContext.getParent() != null){//如果当前applicationContext是容器环境下SpringMVC Application上下文 
    		rootApplicationContext = applicationContext.getParent();
    	}
    	setFinalFieldValue(ApplicationConstants.class, "APPLICATION_CONTEXT", rootApplicationContext);
    	SpringUtils.setApplicationContext(rootApplicationContext);
		Messages.setMessageSource(rootApplicationContext.getBean(AbstractMessageSource.class));
	}

}
