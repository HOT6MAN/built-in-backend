package com.example.hotsix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class HotsixApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HotsixApplication.class);
        app.run(args);
    }
}
