package com.certusnet.xproject.common.mybatis;

import javax.annotation.Resource;

/**
 * DAO基类
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月31日 下午1:18:55
 * @version  	1.0
 */
public abstract class DefaultBaseMybatisDAO {

	@Resource(name="defaultSqlSessionTemplate")
	private EnhancedSqlSession sqlSessionTemplate;

	protected EnhancedSqlSession getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}
	
	protected String getMapperKey(Class<?> modelClass, String key) {
		return MybatisUtils.getMapperKey(modelClass, key);
	}
	
	protected String getMapperKey(String key) {
		return getMapperKey(getBoundModelClass(), key);
	}
	
	/**
	 * 获取绑定在当前DAO上的实体Class
	 * @return
	 */
	protected abstract Class<?> getBoundModelClass();
	
}
