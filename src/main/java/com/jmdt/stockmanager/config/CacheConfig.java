package com.jmdt.stockmanager.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

/**
 * Cache Configuration for Performance Optimization
 * Enables caching for frequently accessed data to improve application performance
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure cache manager with predefined cache names
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Define cache names for different data types
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "businesses",
            "users", 
            "products",
            "customers",
            "sales",
            "inventory",
            "reports",
            "dashboards",
            "kpis",
            "analytics"
        ));
        
        return cacheManager;
    }

    /**
     * Custom key generator for cache keys
     */
    @Bean
    public KeyGenerator customKeyGenerator() {
        return new SimpleKeyGenerator();
    }
}
