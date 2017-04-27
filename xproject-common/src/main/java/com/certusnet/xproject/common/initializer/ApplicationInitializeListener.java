package com.certusnet.xproject.common.initializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.web.context.WebApplicationContext;

import com.certusnet.xproject.common.util.CollectionUtils;
/**
 * spring应用程序监听器
 * 
 * @author  pengpeng
 * @date 	 2015年9月9日 上午12:27:11
 * @version 1.0
 */
public class ApplicationInitializeListener implements ApplicationContextAware, InitializingBean, DisposableBean, PriorityOrdered {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializeListener.class);
	
	private List<ApplicationInitializer> applicationInitializers;
	
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	protected List<ApplicationInitializer> getApplicationInitializers() {
		return applicationInitializers;
	}

	public void setApplicationInitializers(
			List<ApplicationInitializer> applicationInitializers) {
		this.applicationInitializers = applicationInitializers;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void afterPropertiesSet() throws Exception {
		String mode = applicationContext instanceof WebApplicationContext ? "Java Servlet Mode" : "Java Application Mode";
        logger.info(String.format("application initializing start in %s", mode));
        logger.info("application initializing begin ...");
        if (!CollectionUtils.isEmpty(applicationInitializers)) {
            for (ApplicationInitializer applicationInitializer : applicationInitializers) {
            	applicationInitializer.initialize(applicationContext);
            }
        }
        logger.info("application initializing end ...");
	}

	public void destroy() throws Exception {
		String mode = applicationContext instanceof WebApplicationContext ? "Java Servlet Mode" : "Java Application Mode";
    	logger.info(String.format("application destroyed in %s", mode));
	}
	
}
