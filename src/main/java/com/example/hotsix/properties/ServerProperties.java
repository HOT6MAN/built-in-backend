package com.example.hotsix.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("server")
public record ServerProperties(
        String origin
) {
}
