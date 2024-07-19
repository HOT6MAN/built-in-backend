package com.example.hotsix.dto.common;

import com.example.hotsix.enums.Process;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProcessResponse {
    private int statusCode;
    private String message;

    static public ProcessResponse from(Process process) {
        return ProcessResponse.builder()
                .statusCode(process.getHttpStatus().value())
                .message(process.getMessage())
                .build();
    }
}
