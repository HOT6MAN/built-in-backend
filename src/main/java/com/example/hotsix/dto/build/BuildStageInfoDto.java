package com.example.hotsix.dto.build;

import com.example.hotsix.model.project.BuildStage;
import lombok.*;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildStageInfoDto {
    private Long id;
    private String name;
    private String status;
    private Integer duration;

    private List<BuildLogInfoDto> buildLogInfoDtoList;

    public static BuildStageInfoDto from(BuildStage buildStage) {
        return BuildStageInfoDto.builder()
                .id(buildStage.getId())
                .name(buildStage.getName())
                .status(buildStage.getStatus().name())
                .duration(buildStage.getDuration())
                .build();
    }
}
