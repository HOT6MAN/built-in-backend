package com.example.hotsix.repository.build;

import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceScheduleRepository extends JpaRepository<ServiceSchedule, Long>, ServiceScheduleRepositoryCustom {
    ServiceSchedule findByTeamProjectInfo(TeamProjectInfo teamProjectInfo);
}
