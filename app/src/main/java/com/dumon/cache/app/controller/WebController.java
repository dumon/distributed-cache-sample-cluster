package com.dumon.cache.app.controller;

import com.dumon.cache.app.facade.BlogFacade;
import com.google.common.base.Splitter;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;

@Controller
public class WebController {

    @Resource
    private BlogFacade blogFacade;
    @Value("${app.nodes.ports}")
    private String nodePorts;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("blogIds", getBlogsIds());
        populateAvailableNodes(model);
        return "welcome";
    }

    private void populateAvailableNodes(final Map<String, Object> model) {
        Optional.ofNullable(nodePorts).ifPresent(nodes -> {
            List<String> apps = IterableUtils.toList(Splitter.on(":").split(nodes)).stream()
                    .map(port -> "http://localhost:" + port).collect(Collectors.toList());
            model.put("nodes", apps);
        });
    }

    private List<Long> getBlogsIds() {
        return blogFacade.getAllIds();
    }
}
