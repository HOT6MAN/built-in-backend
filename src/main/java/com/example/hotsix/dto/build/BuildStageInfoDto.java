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
    private Long stageId;
    private String name;
    private String status;
    private Integer duration;

    private List<BuildLogInfoDto> buildLogs;

    public static BuildStageInfoDto from(BuildStage buildStage) {
        return BuildStageInfoDto.builder()
                .stageId(buildStage.getId())
                .name(buildStage.getName())
                .status(buildStage.getStatus())
                .duration(buildStage.getDuration())
                .build();
    }
}
