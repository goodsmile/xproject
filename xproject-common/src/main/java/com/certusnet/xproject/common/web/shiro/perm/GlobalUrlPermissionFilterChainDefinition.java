package com.certusnet.xproject.common.web.shiro.perm;

import java.util.Map;
import java.util.Scanner;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.util.Assert;

import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.web.shiro.service.UrlPermissionService;
/**
 * 系统全局url=perms[xxx:yyy]URL与权限配置定义
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月29日 上午9:57:47
 * @version  	1.0
 */
public class GlobalUrlPermissionFilterChainDefinition implements SmartFactoryBean<Ini.Section>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(GlobalUrlPermissionFilterChainDefinition.class);
	
	public static final String DEFAULT_PLACEHOLDER_NAME = "dynamicUrlPermissions";
	
	private String placeHolderName = DEFAULT_PLACEHOLDER_NAME;
	
	private String filterChainDefinitions;
	
	private UrlPermissionService urlPermissionService;
	
	public Section getObject() throws Exception {
		String urlPermissions = filterChainDefinitions;
		String dynamicUrlPermissions = getDynamicUrlPermissions();
		if(!StringUtils.isEmpty(dynamicUrlPermissions)){
			urlPermissions = urlPermissions.replace(buildPlaceHolder(), dynamicUrlPermissions);
		}
		urlPermissions = beautifyUrlPermissionExpression(urlPermissions);
		logger.info(">>> Global URL based permission configuration :\n{}", urlPermissions);
		Ini ini = new Ini();
        ini.load(urlPermissions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if(CollectionUtils.isEmpty(section)){
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
		return section;
	}
	
	protected String getDynamicUrlPermissions() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> allUrlPermissions = urlPermissionService.getAllUrlPermissions();
		if (!CollectionUtils.isEmpty(allUrlPermissions)) {
			for (Map.Entry<String, String> entry : allUrlPermissions.entrySet()) {
				String expression = entry.getKey() + " = " + entry.getValue();
				sb.append(expression);
				if(!expression.endsWith("\n")){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	protected String beautifyUrlPermissionExpression(String expression){
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(expression);
		while(scanner.hasNextLine()){
			String line =  org.apache.shiro.util.StringUtils.clean(scanner.nextLine());
			if (line == null || line.startsWith(Ini.COMMENT_POUND) || line.startsWith(Ini.COMMENT_SEMICOLON)) {
                //skip empty lines and comments:
                continue;
            }
			if(!line.endsWith("\n")){
				line += "\n";
			}
			sb.append("\t\t" + line);
		}
		scanner.close();
		return sb.toString();
	}
	
	protected String buildPlaceHolder() {
		return "${" + placeHolderName + "}";
	}

	public Class<?> getObjectType() {
		return Ini.Section.class;
	}

	public boolean isSingleton() {
		return false;
	}

	public void setPlaceHolderName(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public void setUrlPermissionService(UrlPermissionService urlPermissionService) {
		this.urlPermissionService = urlPermissionService;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.hasText(filterChainDefinitions, "Property 'filterChainDefinitions' can not be empty!");
		Assert.notNull(urlPermissionService, String.format("No bean type of %s defined by Spring!", UrlPermissionService.class));
	}

	public boolean isPrototype() {
		return true;
	}

	public boolean isEagerInit() {
		return false;
	}
	
}
