package com.example.hotsix.service.team;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.project.TeamProjectInfo;

import java.util.List;

public interface TeamProjectInfoService {
    boolean insertEmptyTeamProjectInfo(Long teamId);
    boolean insertAllTeamProjectInfo(Long teamId, Long projectInfoId, ProjectInfoDto projectInfoDto);

    List<TeamProjectInfo> findAllProjectInfosByTeamId(Long teamId);
    boolean saveBackendConfigs(Long teamProjectInfoId, BackendConfigDto[] dtos);
    boolean saveFrontendConfigs(Long teamProjectInfoId, FrontendConfigDto[] dtos);
    boolean saveDatabaseConfigs(Long teamProjectInfoId, DatabaseConfigDto[] dtos);
    boolean updateProjectInfoNameByProjectInfoId(Long projectInfoId, String updateName);
}
