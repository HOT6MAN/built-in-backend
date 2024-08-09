package com.example.hotsix.config;

import com.example.hotsix.properties.ServerProperties;
import com.example.hotsix.properties.StorageProperties;
import com.example.hotsix.service.storage.FileSystemStorageService;
import com.example.hotsix.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadImagePath = storageProperties.uploadImages();

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadImagePath);
    }
}
