package com.example.hotsix.dto.build;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildResultDto {
    private String jobName;
    // 현재 build시킨 job이, jenkins job 기준 몇 번째로 실행되었는가?
    private Long buildNum;
    // 해당 team_project_info 기준으로 몇 번째로 실행되었는가?
    private Long teamProjectInfoId;
    private Integer serviceNum;
    private String result;
    private String jobType;
}
