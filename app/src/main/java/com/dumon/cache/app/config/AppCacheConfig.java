package com.dumon.cache.app.config;

import com.dumon.cache.BlogDto;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.ClusteredStoreConfiguration;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ServerSideConfigurationBuilder;
import org.ehcache.clustered.common.Consistency;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.cache.Caching;

@Configuration
@EnableCaching
@CacheConfig(cacheNames={AppCacheConfig.BLOGS_CACHE})
public class AppCacheConfig extends CachingConfigurerSupport {
    public static final String BLOGS_CACHE = "blogs";
    private static final String CLUSTER_URI = "terracotta://%s:%s/";

    @Value("${cache.node.host}")
    private String cacheHost;
    @Value("${cache.node.port}")
    private String cachePort;
    @Value("${cache.config.name}")
    private String cacheConfigName;

    @Bean
    @Override
    public CacheManager cacheManager() {
        javax.cache.CacheManager clusteredCacheManager = createClusteredCacheManager();
        return new JCacheCacheManager(clusteredCacheManager);
    }

    private javax.cache.CacheManager createClusteredCacheManager() {
        URI clusterUri = URI.create(String.format(CLUSTER_URI, cacheHost, cachePort));
        Consistency consistency = Consistency.STRONG;
        long sizeInMb = 10;
        int ttl = 5;
        long size = 100;

        String offheap = "offheap-1";

        final CacheConfigurationBuilder<Long, BlogDto> configurationBuilder = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, BlogDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .with(ClusteredResourcePoolBuilder
                                        .clusteredDedicated(offheap, 32, MemoryUnit.MB)));

        final ServerSideConfigurationBuilder cfgBuilder =
                ClusteringServiceConfigurationBuilder.cluster(clusterUri.resolve(cacheConfigName))
                        .autoCreate()
                        .defaultServerResource(offheap)
                        .resourcePool("resource-pool-b", 32, MemoryUnit.MB, offheap);

        final CacheManagerBuilder<PersistentCacheManager> clusteredCacheManagerBuilder =
                CacheManagerBuilder.newCacheManagerBuilder()
                        .with(cfgBuilder)
                        .withCache("clustered-cache", configurationBuilder);

        ResourcePoolsBuilder.newResourcePoolsBuilder();
        org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(size).with(ClusteredResourcePoolBuilder.clusteredDedicated(sizeInMb, MemoryUnit.MB)))
                .withExpiry(Expirations.timeToLiveExpiration(new org.ehcache.expiry.Duration(ttl, TimeUnit.MINUTES))).add(new ClusteredStoreConfiguration(consistency)).build();

        Map<String, org.ehcache.config.CacheConfiguration<?, ?>> caches = createCacheConfigurations(cacheConfiguration);

        EhcacheCachingProvider provider = getCachingProvider();
        DefaultConfiguration configuration = new DefaultConfiguration(caches, provider.getDefaultClassLoader(), cfgBuilder.build());
        return provider.getCacheManager(provider.getDefaultURI(), configuration);
    }

    private Map<String, org.ehcache.config.CacheConfiguration<?, ?>> createCacheConfigurations(
            final CacheConfiguration<Object, Object> cacheConfiguration) {
        Map<String, org.ehcache.config.CacheConfiguration<?, ?>> caches = new HashMap<>();
        caches.put(BLOGS_CACHE, cacheConfiguration);
        return caches;
    }

    private EhcacheCachingProvider getCachingProvider() {
        return (EhcacheCachingProvider) Caching.getCachingProvider();
    }
}
