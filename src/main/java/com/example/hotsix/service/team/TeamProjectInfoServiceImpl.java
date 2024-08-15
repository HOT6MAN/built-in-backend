package com.example.hotsix.service.team;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.DatabaseConfigSql;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.project.BackendConfig;
import com.example.hotsix.model.project.DatabaseConfig;
import com.example.hotsix.model.project.FrontendConfig;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.repository.build.BuildRepository;
import com.example.hotsix.repository.build.DatabaseConfigRepository;
import com.example.hotsix.repository.build.DatabaseConfigSqlRepository;
import com.example.hotsix.repository.team.TeamProjectInfoRepository;
import com.example.hotsix.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamProjectInfoServiceImpl implements TeamProjectInfoService{
    private final BuildRepository buildRepository;
    private final TeamProjectInfoRepository teamProjectInfoRepository;
    private final TeamRepository teamRepository;
    private final DatabaseConfigRepository databaseConfigRepository;
    private final DatabaseConfigSqlRepository databaseConfigSqlRepository;

    @Override
    @Transactional
    public TeamProjectInfo insertEmptyTeamProjectInfo(Long teamId, String title){
        TeamProjectInfo teamProjectInfo = TeamProjectInfo.builder()
                .title(title)
                .build();
        Team team = teamRepository.findTeamById(teamId);

        // 양방향 편의 메서드 사용
        teamProjectInfo.setTeam(team);

        // 데이터 저장
        teamProjectInfoRepository.save(teamProjectInfo);

        return teamProjectInfo;
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
    public DatabaseConfigSql findDatabaseConfigSqlByDatabaseConfigId(Long databaseConfigId) {
        DatabaseConfigSql sql = databaseConfigSqlRepository.findDatabaseConfigSqlByDatabaseConfig(databaseConfigId);
        return null;
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

    @Override
    @Transactional
    public boolean saveDatabaseConfigs(Long teamProjectInfoId, Map<Integer, DatabaseConfigDto> configMap, Map<Integer, MultipartFile> fileMap) {
        try {
            TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(teamProjectInfoId);
            if (teamProjectInfo == null) {
                throw new IllegalArgumentException("TeamProjectInfo not found for id: " + teamProjectInfoId);
            }

            List<Integer> sortedIndices = new ArrayList<>(configMap.keySet());
            sortedIndices.sort(Collections.reverseOrder());

            List<DatabaseConfig> existingConfigs = teamProjectInfo.getDatabaseConfigs();
            List<DatabaseConfig> updatedConfigs = new ArrayList<>();

            for (Integer index : sortedIndices) {
                DatabaseConfigDto dto = configMap.get(index);
                MultipartFile file = fileMap.get(index);

                DatabaseConfig config;
                if (dto.getId() != null) {
                    config = existingConfigs.stream()
                            .filter(c -> c.getId().equals(dto.getId()))
                            .findFirst()
                            .orElse(null);
                    if (config != null) {
                        config.setProperties(dto);
                    } else {
                        config = DatabaseConfig.fromDto(dto);
                        config.setProjectInfo(teamProjectInfo);
                    }
                } else {
                    config = DatabaseConfig.fromDto(dto);
                    config.setProjectInfo(teamProjectInfo);
                }

                updatedConfigs.add(config);

                if (file != null && !file.isEmpty()) {
                    processFile(config, file);
                }

                databaseConfigRepository.save(config);
            }

            existingConfigs.removeIf(config -> !updatedConfigs.contains(config));
            existingConfigs.addAll(updatedConfigs.stream()
                    .filter(config -> !existingConfigs.contains(config))
                    .collect(Collectors.toList()));

            teamProjectInfoRepository.save(teamProjectInfo);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void processFile(DatabaseConfig config, MultipartFile file) throws IOException {
        String today = new SimpleDateFormat("yyMMdd").format(new Date());
        String uploadDir = "/spring/sql";
        String imgDirPath = uploadDir + File.separator + today;
        File folder = new File(imgDirPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Failed to create directory: " + imgDirPath);
        }

        DatabaseConfigSql sql = config.getDatabaseConfigSQL();
        if (sql == null) {
            sql = new DatabaseConfigSql();
            sql.setDatabaseConfig(config);
            config.setDatabaseConfigSQL(sql);
        }

        String originName = file.getOriginalFilename();
        if (originName != null && !originName.isEmpty()) {
            String extension = originName.substring(originName.lastIndexOf("."));
            String fixedFileName = UUID.randomUUID().toString() + extension;
            sql.setOriginName(originName);
            sql.setFixedName(fixedFileName);
            sql.setSaveFolder(today);
            File destFile = new File(folder, fixedFileName);
            file.transferTo(destFile);

            if (!destFile.exists() || destFile.length() != file.getSize()) {
                throw new IOException("File transfer failed or file corrupted: " + fixedFileName);
            }
        }

        databaseConfigSqlRepository.save(sql);
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
