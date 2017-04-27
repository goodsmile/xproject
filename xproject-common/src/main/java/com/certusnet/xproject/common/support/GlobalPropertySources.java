package com.certusnet.xproject.common.support;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
/**
 * 应用的全局PropertySources
 * 当中包含[environmentProperties[systemProperties  - JVM系统属性,即System.getProperties(),
 *                                systemEnvironment	- 操作系统环境变量,即System.getenv()], 
 *          localProperties - 一般情况是应用中指定的一系列properties属性配置文件中的内容]
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月17日 下午1:12:34
 * @version  	1.0
 */
public class GlobalPropertySources extends MutablePropertySources implements EnvironmentAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(GlobalPropertySources.class);

	private Environment environment;

	private Resource[] locations;

	private String fileEncoding;

	private boolean ignoreResourceNotFound = false;

	private boolean localOverride = false;

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public void setLocalOverride(boolean localOverride) {
		this.localOverride = localOverride;
	}

	protected void mergePropertySource() throws Exception {
		if (this.environment != null) {
			this.addLast(new PropertySource<Environment>(PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.environment) {
				public String getProperty(String key) {
					return this.source.getProperty(key);
				}
			});
		} else {
			logger.warn("The injected environment was null!");
		}
		if (this.locations != null && this.locations.length > 0) {
			Properties localProperties = new Properties();
			loadProperties(localProperties);
			PropertySource<?> localPropertySource = new PropertiesPropertySource(PropertySourcesPlaceholderConfigurer.LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, localProperties);
			if (this.localOverride) {
				this.addFirst(localPropertySource);
			} else {
				this.addLast(localPropertySource);
			}
		}
	}

	protected void loadProperties(Properties props) throws IOException {
		if (this.locations != null) {
			for (Resource location : this.locations) {
				logger.info("Loading properties file from " + location);
				try {
					PropertiesLoaderUtils.fillProperties(props,
							new EncodedResource(location, this.fileEncoding));
				} catch (IOException ex) {
					if (this.ignoreResourceNotFound) {
						logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
					} else {
						throw ex;
					}
				}
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		mergePropertySource();
	}

}
