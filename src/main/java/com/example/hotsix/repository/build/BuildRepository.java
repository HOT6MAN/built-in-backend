package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildResult;
import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildRepository extends JpaRepository<BuildResult, Long>, BuildRepositoryCustom {
    Optional<BuildResult> findByDeployNumAndTeamProjectInfoId(Long deployNum, Long teamProjectInfoId);

    List<BuildResult> findByTeamProjectInfoId(Long teamProjectInfoId);

}
