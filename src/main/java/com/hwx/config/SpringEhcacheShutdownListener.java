package com.hwx.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: Huawei Xie
 * @date: 2019/5/28
 */

@Component
public class SpringEhcacheShutdownListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            ApplicationContext context = ((ContextClosedEvent) event).getApplicationContext();
            EhCacheManager ehCacheManager = (EhCacheManager) context.getBean("ehCacheManager");
            if (ehCacheManager != null) {
                ehCacheManager.destroy();
            }
        }
    }
}
