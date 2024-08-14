package com.example.hotsix.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JenkinsJobType {
    // 생각하지 못한 에러가 발생하였을 경우 발생하는 ProcessInfo
    SETUP(1, "기존 job shutdown 단계"),
    BACKEND(2, "backend job 단계"),
    FRONTEND(3, "frontend job 단계"),
    DATABASE(4, "database job 단계"),
    FINAL(5, "최종 배포 단계");

    private final int order;
    private final String description;
}
