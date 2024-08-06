package com.example.hotsix.dto.build;

import com.example.hotsix.model.project.BuildLog;
import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildLogInfoDto {
    private Long id;
    private String title;
    private String description;

    public static BuildLogInfoDto from(BuildLog buildLog) {
        return BuildLogInfoDto.builder()
                .id(buildLog.getId())
                .title(buildLog.getTitle())
                .description(buildLog.getDescription())
                .build();
    }
}
