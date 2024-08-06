package com.example.hotsix.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BackendJobName {
    JAVA_SPRING("built_in_backend_docker");

    private final String jobName;
}
