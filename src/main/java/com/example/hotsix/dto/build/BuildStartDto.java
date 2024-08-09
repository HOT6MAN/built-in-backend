package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildStartDto {
    private String buildStatus;
    private Integer serviceNum;

}
