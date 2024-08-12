package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildResult;
import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildResultRepository extends JpaRepository<BuildResult, Long> {
    List<BuildResult> findAllByTeamProjectInfoOrderByDeployNumDesc(TeamProjectInfo teamProjectInfo);

    BuildResult findBuildResultByTeamProjectInfoIdAndDeployNum(Long teamProjectInfoId, Long deployNum);
}
