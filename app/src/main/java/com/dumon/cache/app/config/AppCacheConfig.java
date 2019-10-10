package com.dumon.cache.app.config;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@CacheConfig(cacheNames={AppCacheConfig.BLOGS_CACHE})
public class AppCacheConfig {
    public static final String BLOGS_CACHE = "blogs";
}
