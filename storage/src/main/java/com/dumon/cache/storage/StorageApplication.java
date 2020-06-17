package com.dumon.cache.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class StorageApplication extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(StorageApplication.class);

    @Value("${node.name}")
    private String nodeName;

    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StorageApplication.class);
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> startupLoggingListener() {
        return event -> LOG.info("[SUCCESS_RUN] {}", nodeName);
    }
}
