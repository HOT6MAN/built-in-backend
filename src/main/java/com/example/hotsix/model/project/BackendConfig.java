package com.example.hotsix.model.project;

import com.example.hotsix.dto.build.BackendConfigDto;
import com.example.hotsix.enums.BackendJobName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity(name = "backend_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackendConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_info_id")
    private TeamProjectInfo projectInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "backend_job_name")
    private BackendJobName backendJobName;

    @Column(name = "config_name")
    private String configName;

    @Column(name = "git_url")
    private String gitUrl;

    @Column(name = "git_branch")
    private String gitBranch;
    @Column(name = "git_username")
    private String gitUsername;
    @Column(name = "git_access_token")
    private String gitAccessToken;

    @Column(name = "language")
    private String language;

    @Column(name = "language_version")
    private String languageVersion;

    @Column(name = "framework")
    private String framework;

    @Column(name = "build_tool")
    private String buildTool;

    public void setProperties(BackendConfigDto dto) {
        this.configName = dto.getConfigName();
        this.gitUrl = dto.getGitUrl();
        this.gitBranch = dto.getGitBranch();
        this.gitUsername = dto.getGitUsername();
        this.gitAccessToken = dto.getGitAccessToken();
        this.language = dto.getLanguage();
        this.languageVersion = dto.getLanguageVersion();
        this.framework = dto.getFramework();
        this.buildTool = dto.getBuildTool();
    }

    public BackendConfigDto toDto() {
        return BackendConfigDto.builder()
                .id(id)
                .teamProjectInfoId(projectInfo.getId())
                .configName(configName)
                .gitUrl(gitUrl)
                .gitBranch(gitBranch)
                .gitAccessToken(gitAccessToken)
                .gitUsername(gitUsername)
                .language(language)
                .languageVersion(languageVersion)
                .framework(framework)
                .buildTool(buildTool)
                .build();
    }

    public static BackendConfig fromDto(BackendConfigDto dto) {
        return BackendConfig.builder()
                .id(dto.getId())
                .configName(dto.getConfigName())
                .gitUrl(dto.getGitUrl())
                .gitBranch(dto.getGitBranch())
                .gitUsername(dto.getGitUsername())
                .gitAccessToken(dto.getGitAccessToken())
                .language(dto.getLanguage())
                .languageVersion(dto.getLanguageVersion())
                .framework(dto.getFramework())
                .buildTool(dto.getBuildTool())
                .build();
    }
}

