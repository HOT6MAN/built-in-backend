package com.example.hotsix.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildStatus {
    SUCCESS("성공"),
    FAILED("실패");

    private final String status;
}
