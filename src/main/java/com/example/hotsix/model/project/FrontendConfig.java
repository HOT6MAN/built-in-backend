package com.example.hotsix.model.project;

import com.example.hotsix.dto.build.FrontendConfigDto;
import com.example.hotsix.enums.BackendJobName;
import com.example.hotsix.enums.FrontendJobName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity(name = "frontend_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrontendConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_info_id")
    private TeamProjectInfo projectInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "frontend_job_name")
    private FrontendJobName frontendJobName;

    @Column(name = "framework")
    private String framework;

    @Column(name = "version")
    private String version;
    @Column(name = "git_url")
    private String gitUrl;

    @Column(name = "git_branch")
    private String gitBranch;
    @Column(name = "git_username")
    private String gitUsername;
    @Column(name = "git_access_token")
    private String gitAccessToken;

    public FrontendConfigDto toDto() {
        return FrontendConfigDto.builder()
                .id(id)
                .framework(framework)
                .version(version)
                .gitUrl(gitUrl)
                .gitBranch(gitBranch)
                .gitUsername(gitUsername)
                .gitAccessToken(gitAccessToken)
                .build();
    }

    public static FrontendConfig fromDto(FrontendConfigDto dto) {
        return FrontendConfig.builder()
                .id(dto.getId())
                .framework(dto.getFramework())
                .version(dto.getVersion())
                .gitUrl(dto.getGitUrl())
                .gitBranch(dto.getGitBranch())
                .gitUsername(dto.getGitUsername())
                .gitAccessToken(dto.getGitAccessToken())
                .build();
    }
    public void setProperties(FrontendConfigDto dto) {
        this.framework = dto.getFramework();
        this.version = dto.getVersion();
        this.gitUrl = dto.getGitUrl();
        this.gitBranch = dto.getGitBranch();
        this.gitUsername = dto.getGitUsername();
        this.gitAccessToken = dto.getGitAccessToken();
    }
}
