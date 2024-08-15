package com.example.hotsix.repository.build;

import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceScheduleRepository extends JpaRepository<ServiceSchedule, Long>, ServiceScheduleRepositoryCustom {
    ServiceSchedule findByTeamProjectInfo(TeamProjectInfo teamProjectInfo);

    List<ServiceSchedule> findAllByTeam(Team team);
}
