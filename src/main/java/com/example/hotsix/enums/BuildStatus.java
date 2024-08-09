package com.example.hotsix.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildStatus {
    EMPTY("빈 포트"),
    SUCCESS("성공"),
    PENDING("빌드 중"),
    FAILED("실패");

    private final String status;
}
