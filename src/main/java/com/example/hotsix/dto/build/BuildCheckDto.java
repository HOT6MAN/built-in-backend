package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildCheckDto {
    private Long serviceNum;
}
