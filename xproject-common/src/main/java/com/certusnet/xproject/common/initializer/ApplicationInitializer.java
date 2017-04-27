package com.certusnet.xproject.common.initializer;

import org.springframework.context.ApplicationContext;

/**
 * 应用程序初始化接口
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月14日 下午4:00:50
 * @version  	1.0
 */
public interface ApplicationInitializer {

    /**
     * 执行初始化
     * @param applicationContext
     */
    public void initialize(ApplicationContext applicationContext) throws Exception;

}
