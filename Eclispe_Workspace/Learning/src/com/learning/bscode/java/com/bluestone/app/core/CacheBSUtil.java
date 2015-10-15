package com.bluestone.app.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.generator.ConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 12/12/12
 */

public class CacheBSUtil {

    private static final Logger log = LoggerFactory.getLogger(CacheBSUtil.class);

    public static List<CacheDetails> getResults() {
        log.info("CacheBSUtil.getResults()");
        List<CacheDetails> cacheDetails = new ArrayList<CacheDetails>();
        final CacheManager cacheManager = getCacheManager();
        final String[] cacheNames = cacheManager.getCacheNames();
        long totalBytesHeldInCache = 0l;

        for (int i = 0; i < cacheNames.length; i++) {
            String eachName = cacheNames[i];
            CacheDetails eachCacheStat = getDetails(cacheManager, eachName);
            cacheDetails.add(eachCacheStat);
            totalBytesHeldInCache = totalBytesHeldInCache + eachCacheStat.getTotalSize();
        }
        log.info("***** CacheBSUtil: Total Cache Size = {} bytes **********", totalBytesHeldInCache);
        return cacheDetails;
    }


    public static CacheDetails getResults(String cacheName) {
        List<CacheManager> allList = CacheManager.ALL_CACHE_MANAGERS;
        final CacheManager instance = allList.get(0);
        return getDetails(instance, cacheName);
    }

    private static CacheDetails getDetails(CacheManager instance, String cacheName) {
        log.debug("CacheBSUtil.getDetails() for cache={}", cacheName);
        Cache cache = instance.getCache(cacheName);
        Statistics statistics = cache.getStatistics();
        final long cacheHits = statistics.getCacheHits();
        final long cacheMisses = statistics.getCacheMisses();
        final long objectCount = statistics.getObjectCount();
        if (objectCount > 0) {
            long calculateInMemorySize = cache.calculateInMemorySize();
            long averageSize = calculateInMemorySize / objectCount;
            CacheDetails cacheDetails = new CacheDetails(cacheName, cacheHits, calculateInMemorySize, averageSize, objectCount, cacheMisses);
            log.info(cacheDetails.toString());
            return cacheDetails;
        }
        return new CacheDetails(cacheName, cacheHits, 0, 0, 0, cacheMisses);
    }

    public static void logCacheConfiguration() {
        CacheManager cacheManager = getCacheManager();
        String[] cacheNames = cacheManager.getCacheNames();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cacheNames.length; i++) {
            String eachCache = cacheNames[i];
            Cache cache = cacheManager.getCache(eachCache);
            CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
            stringBuilder.append(ConfigurationUtil.generateCacheConfigurationText(cacheConfiguration));
        }
        log.info("\n{}", stringBuilder.toString());
    }

    static CacheManager getCacheManager() {
        List<CacheManager> allList = CacheManager.ALL_CACHE_MANAGERS;
        log.info("No. of cache manager found = {}", allList.size());
        return allList.get(0);
    }
}


