package com.example.hotsix.dto.build;

import com.example.hotsix.model.project.TeamProjectInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.Builder;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BackendConfigDto {
    private Long id;
    private Long teamProjectInfoId;
    private String configName;
    private String gitUrl;
    private String gitBranch;
    private String gitUsername;
    private String gitAccessToken;
    private String language;
    private String languageVersion;
    private String framework;
    private String buildTool;

}
