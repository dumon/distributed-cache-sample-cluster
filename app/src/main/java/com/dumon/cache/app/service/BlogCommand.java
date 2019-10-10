package com.dumon.cache.app.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public abstract class BlogCommand<T> {

    private static final String FULL_URL_COMMAND = "http://%s:%s/blogs";
    private static final Gson GSON = new Gson();


    public abstract List<T> execute(final List<T> requested);

    public List<T> execute() {
        return execute(Lists.newArrayList());
    }

    protected abstract HttpMethod getMethod();

    public List<T> execute(final String url, final List<T> requested) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestedEntities = new HttpEntity<>(GSON.toJson(requested));

        ResponseEntity<List<T>> cmd = restTemplate.exchange(
                url,
                getMethod(),
                requested.isEmpty() ? null : requestedEntities,
                getResponseType());

        return cmd.getBody();
    }

    protected abstract ParameterizedTypeReference<List<T>> getResponseType();

    protected String buildUrl(final String host, final String port, final String path,
                              final Map<String, String> params) {
        String url = String.format(FULL_URL_COMMAND, host, port);
        if (StringUtils.isNotBlank(path)) {
            url += "/" + path;
        }
        if (MapUtils.isNotEmpty(params)) {
            List<String> paramStrings = Lists.newArrayList();
            params.forEach((paramName, paramValue) -> paramStrings.add(paramName + "=" + paramValue));
            url += "?" + Joiner.on("&").join(paramStrings);
        }
        return url;
    }
}
