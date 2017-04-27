package com.certusnet.xproject.common.initializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.certusnet.xproject.common.support.ServletContextAttribute;
import com.certusnet.xproject.common.util.ClassScanningUtils;
import com.certusnet.xproject.common.util.ClassUtils;
import com.certusnet.xproject.common.util.CollectionUtils;

/**
 * 将标有@ServletContextAttribute注解的系统常量set到ServletContext.attributes中去,之后可以在页面中获得e.g. ${applicationScope.XXXX}
 *
 * 注：由于该初始化程序中涉及到容器上下文(ServletContext)即需要Servlet容器环境的支持,因此该初始化bean必须配置在springmvc配置文件中
 * @author 	pengpeng
 * @version 	1.0
 * @date 		2014-07-03 下午3:52:15
 */
public class ServletContextAttributeInitializer extends AbstractApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServletContextAttributeInitializer.class);

    private String basePackage;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize(ApplicationContext applicationContext) throws Exception {
    	WebApplicationContext webApplicationContext = null;
    	if(applicationContext instanceof WebApplicationContext && applicationContext.getParent() != null){
    		webApplicationContext = (WebApplicationContext) applicationContext;
    		ServletContext servletContext = webApplicationContext.getServletContext();
    		logger.info(">>> 初始化Servlet上下文级别的Attribute!");
            List<String> classNameList = ClassScanningUtils.scanPackages(basePackage);
            if (!CollectionUtils.isEmpty(classNameList)) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                for (String className : classNameList) {
                    Class<?> clazz = ClassUtils.forName(className, Thread.currentThread().getContextClassLoader());
                    Field[] fields = clazz.getFields();
                    if (clazz.isAnnotationPresent(ServletContextAttribute.class)) {
                        if (fields != null) {
                            for (Field field : fields) {
                                if (field.isEnumConstant() || Modifier.isFinal(field.getModifiers())) {
                                    servletContext.setAttribute(field.getName(), field.get(null));
                                }
                            }
                        }
                    } else {
                        if (fields != null) {
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(ServletContextAttribute.class)) {
                                    if (field.isEnumConstant() || Modifier.isFinal(field.getModifiers())) {
                                        servletContext.setAttribute(field.getName(), field.get(null));
                                    }
                                }
                            }
                        }
                    }
                }
            }
    	}
    }

}
