package com.example.hotsix.repository.build;

import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.project.TeamProjectInfo;

import java.util.List;

public interface ServiceScheduleRepositoryCustom {

    ServiceSchedule findServiceScheduleByServiceScheduleId(Long serviceScheduleId);
    ServiceSchedule findEmptyService(TeamProjectInfo teamProjectInfo);
    List<TeamProjectInfo> findUsedProjectInfoIdByTeamId(Long teamId);
}
