package com.bluestone.app.admin.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.core.CacheBSUtil;
import com.bluestone.app.core.CacheDetails;

@Controller
@RequestMapping("/admin/cache*")
public class AdminCacheController {

    @Autowired
    private CacheManager cacheManager;

    private static final Logger log = LoggerFactory.getLogger(AdminCacheController.class);

    @PostConstruct
    void init(){
        log.debug("AdminCacheController.init()");
        CacheBSUtil.logCacheConfiguration();
    }

    @RequestMapping(value = "/clearall", method = RequestMethod.GET)
    @RequiresPermissions("cache:delete")
    public
    @ResponseBody
    String clearCache(HttpServletRequest httpServletRequest) {
        log.info("Clearing all Cache Initiated...........");

        String[] cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            log.info("AdminCacheController.clearCache(): Clearing cache={}", cacheName);
            Cache cache = cacheManager.getCache(cacheName);
            cache.removeAll();
        }
        log.info("All Caches Cleared........");
        return "All Caches Cleared";
    }

    @RequestMapping(value = "/clear/{cacheName}", method = RequestMethod.GET)
    @RequiresPermissions("cache:delete")
    public
    @ResponseBody
    String clearCache(@PathVariable String cacheName, HttpServletRequest httpServletRequest) {
        log.info("Clearing Cache={} Initiated...........", cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
            log.info("Cache={} Cleared........", cache);
        } else {
            log.warn("Cache={} does not exist.", cache);
        }
        return "Operation Finished";
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @RequiresPermissions("cache:view")
    public
    @ResponseBody
    String cacheDetails(HttpServletRequest httpServletRequest) {
        log.info("Get All Cache Details Initiated.........");
        List<CacheDetails> results = CacheBSUtil.getResults();
        log.info("All Caches Details fetched.");
        return new Gson().toJson(results);
    }

    @PreDestroy
    public void dumpCacheStats() {
        log.info("AdminCacheController.dumping cache stats as a part of pre destroy hook");
        List<CacheDetails> results = CacheBSUtil.getResults();
        for (CacheDetails cacheDetails : results) {
            log.info(cacheDetails.toString());
        }
    }

}
