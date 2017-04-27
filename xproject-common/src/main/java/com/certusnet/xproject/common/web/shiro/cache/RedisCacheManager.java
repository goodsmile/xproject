package com.certusnet.xproject.common.web.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisCacheManager extends AbstractCacheManager implements InitializingBean, DisposableBean {

	public static final String DEFAULT_SHIRO_REDIS_KEY_PREFIX = "shiro:";
	
	private RedisTemplate<String,Object> redisTemplate;
	
	private boolean cacheExpire = true;
	
	private int cacheExpireSeconds = 1800;
	
	private String cacheKeyPrefix = DEFAULT_SHIRO_REDIS_KEY_PREFIX;
	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setCacheExpire(boolean cacheExpire) {
		this.cacheExpire = cacheExpire;
	}

	public void setCacheExpireSeconds(int cacheExpireSeconds) {
		this.cacheExpireSeconds = cacheExpireSeconds;
	}

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}

	protected Cache<String,Object> createCache(String cacheName) throws CacheException {
		return new RedisCache(cacheName, redisTemplate, cacheExpire, cacheExpireSeconds, cacheKeyPrefix);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redisTemplate, "Property 'redisTemplate' can not be null!");
	}

}
