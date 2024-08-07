package com.example.hotsix.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API 응답 중, "process" 부분에 담기는 부분을 enum화
 * HttpStatus + Error Message 형태
 */

@Getter
@RequiredArgsConstructor
public enum Process {
    // 정상적인 응답이 반환되었을 경우 사용되는 ProcessInfo
    NORMAL_RESPONSE(HttpStatus.OK, "정상적인 응답을 반환하였습니다"),

    // 로직 상에서 예상되는 에러가 있을 경우, 여기에 정의해서 사용
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 유저입니다"),
    DEAL_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 상품입니다"),
    RECRUIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 모집글 입니다"),
    RESUME_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이력서 입니다"),

    INVALID_USER(HttpStatus.UNAUTHORIZED, "잘못된 JWT Token 입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "유효기간이 만료된 Token입니다."),
    TEAM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 팀입니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    // 생각하지 못한 에러가 발생하였을 경우 발생하는 ProcessInfo
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "원인 모를 에러입니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String toString() {
        return "Process{" +
                "httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                '}';
    }
}
