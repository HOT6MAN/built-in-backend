package com.example.hotsix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@SpringBootApplication
public class HotsixApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HotsixApplication.class);
        app.run(args);
    }
}
