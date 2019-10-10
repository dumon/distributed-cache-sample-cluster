package com.dumon.cache.app.service;

import com.dumon.cache.BlogDto;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BlogService {

    @Value("${data.host}")
    private String dataHost;
    @Value("${data.port}")
    private String dataPort;

    public List<Long> getIds() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                String.format("http://%s:%s/blogs/ids", dataHost, dataPort),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {}).getBody();
    }

    public BlogDto getById(final long id) {
        GetBlogCommand<BlogDto> cmd = buildBlogGetById(id);
        return cmd.execute().get(0);
    }

    public List<BlogDto> getAll() {
        BlogCommand<BlogDto> getAll = buildGetAllBlogsCmd();
        return getAll.execute();
    }

    private GetBlogCommand<BlogDto> buildBlogGetById(final long id) {
        return GetBlogCommand.builder().host(dataHost).port(dataPort)
                .params(ImmutableMap.of("id", String.valueOf(id))).build();
    }

    private GetBlogCommand<BlogDto> buildGetAllBlogsCmd() {
        return GetBlogCommand.builder().host(dataHost).port(dataPort).path("all").build();
    }
}
