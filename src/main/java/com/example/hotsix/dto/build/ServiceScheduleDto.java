package com.example.hotsix.dto.build;

import com.example.hotsix.model.ServiceSchedule;
import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceScheduleDto {
    private Long id;
    private Long teamId;
    private Long teamProjectInfoId;
    private String buildStatus;
    private String grafanaURL;

    public static ServiceScheduleDto fromEntity(ServiceSchedule schedule) {
        if (schedule == null) {
            return null;
        }

        return ServiceScheduleDto.builder()
                .id(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .teamProjectInfoId(schedule.getTeamProjectInfo().getId())
                .buildStatus(schedule.getBuildStatus().name())
                .grafanaURL(schedule.getGrafanaUid())
                .build();
    }
}
