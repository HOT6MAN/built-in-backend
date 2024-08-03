package com.example.hotsix.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
public record StorageProperties(
        String uploadImages
) {
}
