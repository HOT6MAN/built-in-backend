package com.example.hotsix.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DatabaseJobName {
    MYSQL("built_in_database_docker");

    private final String jobName;
}
