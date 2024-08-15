package com.example.hotsix.service.team;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.DatabaseConfigSql;
import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TeamProjectInfoService {
    TeamProjectInfo insertEmptyTeamProjectInfo(Long teamId, String title);
    boolean insertAllTeamProjectInfo(Long teamId, Long projectInfoId, ProjectInfoDto projectInfoDto);

    List<TeamProjectInfo> findAllProjectInfosByTeamId(Long teamId);
    DatabaseConfigSql findDatabaseConfigSqlByDatabaseConfigId(Long databaseConfigId);
    boolean saveBackendConfigs(Long teamProjectInfoId, BackendConfigDto[] dtos);
    boolean saveFrontendConfigs(Long teamProjectInfoId, FrontendConfigDto[] dtos);
    boolean saveDatabaseConfigs(Long teamProjectInfoId, DatabaseConfigDto[] dtos);
    boolean saveDatabaseConfigs(Long teamProjectInfoId, Map<Integer, DatabaseConfigDto> configMap, Map<Integer, MultipartFile> fileMap);
    boolean updateProjectInfoNameByProjectInfoId(Long projectInfoId, String updateName);
}
