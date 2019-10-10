package com.dumon.cache.storage.config;

import com.dumon.cache.storage.entity.Blog;
import com.dumon.cache.storage.helper.LoadHelper;
import com.dumon.cache.storage.repo.BlogRepository;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class DataPreloader {

	@Resource
	private AppProperties appProperties;
	@Resource
	private BlogRepository blogRepository;

	@PostConstruct
	public void configure() throws Exception {
		Optional<String> usersFilePath = LoadHelper.getJvmArg(appProperties.getDataFile());
		List<Blog> blogs;
		if (usersFilePath.isPresent()) {
			blogs = LoadHelper.importBlogsFromFile(usersFilePath.get());
		} else {
			blogs = LoadHelper.importDefaultBlogs();
		}
		blogs.forEach(blogRepository::save);
	}
}
