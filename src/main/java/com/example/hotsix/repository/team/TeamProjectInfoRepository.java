package com.example.hotsix.repository.team;

import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamProjectInfoRepository extends JpaRepository<TeamProjectInfo, Long>, TeamProjectInfoRepositoryCustom {
}
