package com.dumon.cache.app.service;

import com.dumon.cache.BlogDto;
import com.google.common.collect.Maps;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

@Builder
public class GetBlogCommand<T extends BlogDto> extends BlogCommand<T> {

    private String host;
    private String port;
    private String path;
    private Map<String, String> params = Maps.newHashMap();

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public List<T> execute(final List<T> requested) {
        final String url = buildUrl(host, port, path, params);
        return execute(url, requested);
    }

    @Override
    protected ParameterizedTypeReference<List<T>> getResponseType() {
        return new ParameterizedTypeReference<List<T>>() {};
    }
}
