package com.certusnet.xproject.common.web.shiro.credentials;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 密码重试次数限制
 * 
 * @author	  	pengpeng
 * @date	  	2015年1月28日 上午11:05:36
 * @version  	1.0
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	public static final int DEFAULT_PASSWORD_RETRY_LIMIT = 5;
	
	public static final int DEFAULT_PASSWORD_RETRY_WAIT_MINUTES = 30;
	
	public static final String REDIS_KEY_PREFIX = "SHRIO_PASSWORD_RETRY_";
	
	private int passwordRetryLimit = DEFAULT_PASSWORD_RETRY_LIMIT;
	
	private int passwordRetryWaitMinutes = DEFAULT_PASSWORD_RETRY_WAIT_MINUTES;
	
	private RedisTemplate<String,Object> redisTemplate;
	
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String userName = (String)token.getPrincipal();
		final String key = REDIS_KEY_PREFIX + userName;
		long maxRetry = redisTemplate.opsForValue().increment(key, 1);
		if(maxRetry == 1){ //首次输入密码
			redisTemplate.expire(key, passwordRetryWaitMinutes, TimeUnit.MINUTES);
		}
		if(maxRetry >= passwordRetryLimit){
			throw new ExcessiveAttemptsException(passwordRetryLimit + "");
		}
		boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
        	redisTemplate.delete(key);
        }
        return matches;
	}

	public void setPasswordRetryLimit(int passwordRetryLimit) {
		this.passwordRetryLimit = passwordRetryLimit;
	}

	public void setPasswordRetryWaitMinutes(int passwordRetryWaitMinutes) {
		this.passwordRetryWaitMinutes = passwordRetryWaitMinutes;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
