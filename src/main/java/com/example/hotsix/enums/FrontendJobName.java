package com.example.hotsix.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrontendJobName {
    VUE("built_in_frontend_docker");

    private final String jobName;
}
