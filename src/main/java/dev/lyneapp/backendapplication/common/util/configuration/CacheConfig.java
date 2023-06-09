package dev.lyneapp.backendapplication.common.util.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000));;
        return cacheManager;
    }
    
    @Bean
    public Cache matchedUsersCache() {
        return cacheManager().getCache("matchedUsers");
    }

    @Bean
    public Cache likedUsersCache() {
        return cacheManager().getCache("likedUsers");
    }

    @Bean
    public Cache blockedUsersCache() {
        return cacheManager().getCache("blockedUsers");
    }

    @Bean
    public Cache recommendedUsersCache() {
        return cacheManager().getCache("recommendedUsers");
    }
}
