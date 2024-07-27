package com.example.hotsix.dto.common;

import com.example.hotsix.enums.Process;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private Process process;

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "process=" + process +
                '}';
    }
}
