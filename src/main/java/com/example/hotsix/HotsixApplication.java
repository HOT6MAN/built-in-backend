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

        try {
            // 현재 클래스 파일의 경로를 URI로 가져옴
            URI rootUri = HotsixApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI();

            // URI를 Path로 변환
            Path rootPath = Paths.get(rootUri).getParent().getParent().getParent(); // 프로젝트 루트 경로 계산

            // 프로젝트 루트 경로를 사용하여 secure/application.yml 파일 경로 설정
            Path configFilePath = rootPath.resolve("secure/application.yml");
            String configPath = configFilePath.toString();

            System.out.println("Project Root Path: " + rootPath.toAbsolutePath());
            System.out.println("Config File Path: " + configPath);

            // 설정 파일 경로를 지정하는 속성을 설정
            app.setDefaultProperties(Collections.singletonMap("spring.config.location", "file:" + configPath));

            app.run(args);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to determine project root path", e);
        }
    }
}
