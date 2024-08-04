package com.example.hotsix.repository.team;

import com.example.hotsix.model.project.TeamProjectInfo;

import java.util.List;

public interface TeamProjectInfoRepositoryCustom {
    List<TeamProjectInfo> findAllProjectInfosByTeamId(Long teamId);
    TeamProjectInfo findProjectInfoByProjectInfoId(Long projectInfoId);
}
