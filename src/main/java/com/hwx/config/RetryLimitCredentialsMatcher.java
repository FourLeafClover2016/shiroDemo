package com.hwx.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     * 密码输入错误次数就被冻结
     */
    private Integer errorPasswordTimes=5;

    private Cache<String, AtomicInteger> passwordRetryCache;

    /**
     * 构造方法 创建对象,传入缓存的管理器
     * @param cacheManager
     */
    public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    /**
     * 方法名: doCredentialsMatch
     * 方法描述: 用户登录错误次数方法.
     * 修改日期: 2019/2/26 20:19
     * @param token
     * @param info
     * @return boolean
     * @throws
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        Set<String> keys = passwordRetryCache.keys();

        // retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > errorPasswordTimes) {
            // if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            // clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
