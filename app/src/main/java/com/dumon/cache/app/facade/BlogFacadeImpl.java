package com.dumon.cache.app.facade;

import static com.dumon.cache.app.config.AppCacheConfig.BLOGS_CACHE;

import com.dumon.cache.BlogDto;
import com.dumon.cache.app.service.BlogService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

@Service
public class BlogFacadeImpl implements BlogFacade {

    @Resource
    private BlogService blogService;

    @Override
    public List<Long> getAllIds() {
        return blogService.getIds();
    }

    @Cacheable(cacheNames=BLOGS_CACHE, key="#id")
    @Override
    public BlogDto getBlogById(final long id) {
        return blogService.getById(id);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogService.getAll();
    }

    @Override
    public List<Long> getAllTitles() {
        return new ArrayList<>(blogService.getIds());
    }
}
