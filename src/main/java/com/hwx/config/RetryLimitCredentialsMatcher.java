package com.hwx.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Huawei Xie
 * @date: 2019/5/28
 */
//@Component
public class RetryLimitCredentialsMatcher  extends HashedCredentialsMatcher {
    private static final int MAX_LOGIN_RETRY_TIMES = 5;
    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitCredentialsMatcher(EhCacheManager ehCacheManager) {
        passwordRetryCache = ehCacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws ExcessiveAttemptsException{
        String userName = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(userName);
        if (retryCount == null) {
            // 高并发下使用的线程安全的int类
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(userName, retryCount);
        }
        if (retryCount.incrementAndGet() > MAX_LOGIN_RETRY_TIMES) {
            throw new ExcessiveAttemptsException();
        }

        boolean match = super.doCredentialsMatch(token, info);
        if (match) {
            passwordRetryCache.remove(userName);
        }

        return match;
    }


}
