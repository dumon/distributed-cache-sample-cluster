package com.dumon.cache.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Validated
@ConfigurationProperties("app")
@PropertySources({
    @PropertySource("classpath:./config/app.config"),
    @PropertySource(value = "file:${app.config:./app.config}", ignoreResourceNotFound = true)})
public class AppProperties {

    @NotBlank
    private String dataFile;
    @NotNull
    private long responseDelay;

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(final String dataFile) {
        this.dataFile = dataFile;
    }

}
