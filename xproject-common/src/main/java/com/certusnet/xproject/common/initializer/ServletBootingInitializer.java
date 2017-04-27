package com.certusnet.xproject.common.initializer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.certusnet.xproject.common.consts.ApplicationConstants;
import com.certusnet.xproject.common.util.FileUtils;

/**
 * 应用中依赖于Servlet环境的系统常量初始化
 * 
 * 注：由于该初始化程序中涉及到容器上下文(ServletContext)即需要Servlet容器环境的支持,因此该初始化bean必须配置在springmvc配置文件中
 * 
 * @author 	pengpeng
 * @version 	1.0
 * @date 		2014-07-03 下午3:52:15
 */
public class ServletBootingInitializer extends AbstractApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServletBootingInitializer.class);

    public void initialize(ApplicationContext applicationContext) throws Exception {
    	WebApplicationContext mvcApplicationContext = null;
    	if(applicationContext instanceof WebApplicationContext && applicationContext.getParent() != null){
    		mvcApplicationContext = (WebApplicationContext) applicationContext;
    		ServletContext servletContext = mvcApplicationContext.getServletContext();
    		logger.info(">>> 初始化应用中依赖于Servlet环境的系统常量!");
    		setFinalFieldValue(ApplicationConstants.class, "MVC_APPLICATION_CONTEXT", applicationContext);
    		setFinalFieldValue(ApplicationConstants.class, "SERVLET_CONTEXT", servletContext);
    		setFinalFieldValue(ApplicationConstants.class, "CONTEXT_PATH", FileUtils.formatFilePath(servletContext.getContextPath()));
    		setFinalFieldValue(ApplicationConstants.class, "CONTEXT_REAL_PATH", FileUtils.formatFilePath(servletContext.getRealPath("/")));
    	}
    }
}
