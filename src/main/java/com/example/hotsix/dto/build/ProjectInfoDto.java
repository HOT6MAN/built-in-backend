package com.example.hotsix.dto.build;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInfoDto {
    private BackendConfigDto[] backendConfigDtos;
    private FrontendConfigDto[] frontendConfigDtos;
    private DatabaseConfigDto[] databaseConfigDtos;
}
