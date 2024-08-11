package com.example.hotsix.service.team;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.project.BackendConfig;
import com.example.hotsix.model.project.DatabaseConfig;
import com.example.hotsix.model.project.FrontendConfig;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.repository.build.BuildRepository;
import com.example.hotsix.repository.team.TeamProjectInfoRepository;
import com.example.hotsix.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamProjectInfoServiceImpl implements TeamProjectInfoService{
    private final BuildRepository buildRepository;
    private final TeamProjectInfoRepository teamProjectInfoRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public boolean insertEmptyTeamProjectInfo(Long teamId){
        TeamProjectInfo teamProjectInfo = TeamProjectInfo.builder()
                .title("default config")
                .build();
        Team team = teamRepository.findTeamById(teamId);

        team.addTeamProjectInfo(teamProjectInfo);
        teamProjectInfo.setTeam(team);

        teamRepository.save(team);
        return true;
    }
    @Override
    @Transactional
    public boolean insertAllTeamProjectInfo(Long teamId, Long projectInfoId, ProjectInfoDto projectInfoDto) {
        Team team = teamRepository.findTeamById(teamId);
        TeamProjectInfo teamProjectInfo = buildRepository.findTeamProjectInfoByMemberAndInfoId(teamId, projectInfoId);

        updateBackendConfigs(teamProjectInfo, projectInfoDto.getBackendConfigDtos());
        updateFrontendConfigs(teamProjectInfo, projectInfoDto.getFrontendConfigDtos());
        updateDatabaseConfigs(teamProjectInfo, projectInfoDto.getDatabaseConfigDtos());

        return true;
    }
    @Override
    public boolean updateProjectInfoNameByProjectInfoId(Long projectInfoId, String updateName){
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(projectInfoId);
        teamProjectInfo.setTitle(updateName);
        teamProjectInfoRepository.save(teamProjectInfo);
        return true;
    }

    @Override
    public List<TeamProjectInfo> findAllProjectInfosByTeamId(Long teamId) {
        return teamProjectInfoRepository.findAllProjectInfosByTeamId(teamId);
    }

    @Override
    @Transactional
    public boolean saveBackendConfigs(Long teamProjectInfoId, BackendConfigDto[] dtos){
        try{
            TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
            updateBackendConfigs(teamProjectInfo, dtos);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveFrontendConfigs(Long teamProjectInfoId, FrontendConfigDto[] dtos){
        try{
            TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
            updateFrontendConfigs(teamProjectInfo, dtos);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveDatabaseConfigs(Long teamProjectInfoId, DatabaseConfigDto[] dtos){
        try{
            TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
            updateDatabaseConfigs(teamProjectInfo, dtos);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void updateBackendConfigs(TeamProjectInfo teamProjectInfo, BackendConfigDto[] backendConfigDtos) {
        List<BackendConfig> existingConfigs = teamProjectInfo.getBackendConfigs();
        List<BackendConfig> updatedConfigs = new ArrayList<>();
        for (BackendConfigDto dto : backendConfigDtos) {
            if (dto.getId() != null) {
                BackendConfig existConfig = existingConfigs.stream()
                        .filter(c -> c.getId().equals(dto.getId()))
                        .findFirst()
                        .orElse(null);
                if (existConfig != null) {
                    existConfig.setProperties(dto);
                    updatedConfigs.add(existConfig);
                }
            } else {
                BackendConfig newConfig = BackendConfig.fromDto(dto);
                newConfig.setProjectInfo(teamProjectInfo);
                updatedConfigs.add(newConfig);
            }
        }
        existingConfigs.removeIf(config -> !updatedConfigs.contains(config));
        existingConfigs.addAll(updatedConfigs.stream()
                .filter(config -> !existingConfigs.contains(config))
                .collect(Collectors.toList()));
    }

    private void updateFrontendConfigs(TeamProjectInfo teamProjectInfo, FrontendConfigDto[] frontendConfigDtos) {
        List<FrontendConfig> existingConfigs = teamProjectInfo.getFrontendConfigs();
        List<FrontendConfig> updatedConfigs = new ArrayList<>();
        for (FrontendConfigDto dto : frontendConfigDtos) {
            if (dto.getId() != null) {
                FrontendConfig existConfig = existingConfigs.stream()
                        .filter(c -> c.getId().equals(dto.getId()))
                        .findFirst()
                        .orElse(null);
                if (existConfig != null) {
                    existConfig.setProperties(dto);
                    updatedConfigs.add(existConfig);
                }
            } else {
                FrontendConfig newConfig = FrontendConfig.fromDto(dto);
                newConfig.setProjectInfo(teamProjectInfo);
                updatedConfigs.add(newConfig);
            }
        }
        existingConfigs.removeIf(config -> !updatedConfigs.contains(config));
        existingConfigs.addAll(updatedConfigs.stream()
                .filter(config -> !existingConfigs.contains(config))
                .collect(Collectors.toList()));
    }

    private void updateDatabaseConfigs(TeamProjectInfo teamProjectInfo, DatabaseConfigDto[] databaseConfigDtos) {
        List<DatabaseConfig> existingConfigs = teamProjectInfo.getDatabaseConfigs();
        List<DatabaseConfig> updatedConfigs = new ArrayList<>();
        for (DatabaseConfigDto dto : databaseConfigDtos) {
            if (dto.getId() != null) {
                DatabaseConfig existConfig = existingConfigs.stream()
                        .filter(c -> c.getId().equals(dto.getId()))
                        .findFirst()
                        .orElse(null);
                if (existConfig != null) {
                    existConfig.setProperties(dto);
                    updatedConfigs.add(existConfig);
                }
            } else {
                DatabaseConfig newConfig = DatabaseConfig.fromDto(dto);
                newConfig.setProjectInfo(teamProjectInfo);
                updatedConfigs.add(newConfig);
            }
        }
        existingConfigs.removeIf(config -> !updatedConfigs.contains(config));
        existingConfigs.addAll(updatedConfigs.stream()
                .filter(config -> !existingConfigs.contains(config))
                .collect(Collectors.toList()));
    }
}
