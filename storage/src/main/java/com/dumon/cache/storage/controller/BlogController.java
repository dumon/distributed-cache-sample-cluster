package com.dumon.cache.storage.controller;

import com.dumon.cache.storage.entity.Blog;
import com.dumon.cache.storage.repo.BlogRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

@RestController
@RequestMapping(
        value = "/blogs",
        produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE
        })
public class BlogController {
    private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

    @Resource
    private BlogRepository blogRepository;

    @Value("${app.response.delay}")
    private long delay;

    @GetMapping("/all")
    public List<Blog> getAll() {
        LOG.info("getting all blogs");
        delay();
        return IterableUtils.toList(blogRepository.findAll());
    }

    @GetMapping("/ids")
    public List<Long> getIds() {
        LOG.info("getting all blogs");
        return IterableUtils.toList(blogRepository.findAll())
                .stream().map(Blog::getId).collect(Collectors.toList());
    }

    @GetMapping("/titles")
    public List<String> getAllTitles() {
        LOG.info("getting all blogs' titles");
        delay();
        return IterableUtils.toList(blogRepository.findAll())
                .stream().map(Blog::getTitle).collect(Collectors.toList());
    }

    @GetMapping
    public List<Blog> getById(@RequestParam long id) {
        LOG.info("getting blogs by id {}", id);
        delay();
        List<Blog> result = new ArrayList<>();
        blogRepository.findById(id).ifPresent(result::add);
        return result;
    }

    private void delay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
