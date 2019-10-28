package com.dumon.cache.app.controller;

import static com.dumon.cache.app.config.AppCacheConfig.BLOGS_CACHE;

import com.dumon.cache.BlogDto;
import com.dumon.cache.app.facade.BlogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping(
        value = "/app",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ApplicationController {
    @Autowired
    private CacheManager cacheManager;
    @Resource
    private BlogFacade blogFacade;

    @PostConstruct
    public void resetCacheOnLoad() {
        cacheManager.getCache(BLOGS_CACHE).clear();
    }

    @GetMapping("/clear")
    public void evictCache() {
        cacheManager.getCache(BLOGS_CACHE).clear();
    }

    @GetMapping("/load/{id}")
    public BlogDto evictCache(@PathVariable("id") final long id) {
        return blogFacade.getBlogById(id);
    }
}
