package com.example.hotsix.dto.build;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TeamProjectInfoDto {
    private Long id;
    private String title;
    private List<BackendConfigDto> backendConfigs;
    private List<FrontendConfigDto> frontendConfigs;
    private List<DatabaseConfigDto> databaseConfigs;
}
