package com.example.hotsix.exception;

import com.example.hotsix.enums.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Custom Exception
 * 로직에서 예상되는 예외가 있을 시, 이 에러를 던지도록 사용
 */

@Getter
@RequiredArgsConstructor
public class BuiltInException extends RuntimeException {
    private final Process process;

    @Override
    public String toString() {
        return "BuiltInException{" +
                "process=" + process +
                '}';
    }
}