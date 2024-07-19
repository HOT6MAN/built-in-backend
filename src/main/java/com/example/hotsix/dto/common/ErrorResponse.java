package com.example.hotsix.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private ProcessResponse processResponse;
}
