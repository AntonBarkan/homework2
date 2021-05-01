package com.exercise.anton.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "anton", ignoreUnknownFields = false)
@Setter
@Getter
public class AppProperties {

    private String server;
}
