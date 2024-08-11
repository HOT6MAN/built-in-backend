package com.example.hotsix.dto.build;

import jakarta.persistence.Column;
import lombok.*;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FrontendConfigDto {
    private Long id;
    private Long teamProjectInfoId;
    private String configName;
    private String framework;
    private String version;
    private String gitUrl;
    private String gitBranch;
    private String gitUsername;
    private String gitAccessToken;
}
