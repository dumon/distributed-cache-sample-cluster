package com.dumon.cache.app.facade;

import com.dumon.cache.BlogDto;

import java.util.List;

public interface BlogFacade {
    List<Long> getAllIds();
    List<BlogDto> getAllBlogs();
    List<Long> getAllTitles();
    BlogDto getBlogById(long id);
}
