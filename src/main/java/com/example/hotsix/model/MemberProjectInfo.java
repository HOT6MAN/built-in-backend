package com.example.hotsix.model;


import com.example.hotsix.dto.build.MemberProjectInfoDto;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.stereotype.Service;

@Entity(name = "member_project_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @Column(name = "backend_git_url")
    private String backendGitUrl;
    @Column(name = "backend_git_branch")
    private String backendGitBranch;
    @Column(name = "backend_language")
    private String backendLanguage;
    @Column(name = "backend_framework")
    private String backendFramework;
    @Column(name = "backend_build_tool")
    private String backendBuildTool;
    @Column(name = "frontend_framework")
    private String frontendFramework;
    @Column(name = "frontend_build_tool")
    private String frontendBuildTool;
    @Column(name = "frontend_git_url")
    private String frontendGitUrl;
    @Column(name = "frontend_git_branch")
    private String frontendGitBranch;
    @Column(name = "database_name")
    private String databaseName;

    public MemberProjectInfoDto toDto(){
        return MemberProjectInfoDto.builder()
                .id(id)
                .backendGitURL(backendGitUrl)
                .backendGitBranch(backendGitBranch)
                .backendLanguage(backendLanguage)
                .backendFramework(backendFramework)
                .backendBuildTool(backendBuildTool)
                .frontendFramework(frontendFramework)
                .frontendBuildTool(frontendBuildTool)
                .frontendGitUrl(frontendGitUrl)
                .frontendGitBranch(frontendGitBranch)
                .databaseName(databaseName)
                .build();
    }

    public void setPropertiesFromDto(MemberProjectInfoDto dto){
        this.id = dto.getId();
        this.backendGitUrl = dto.getBackendGitURL();
        this.backendGitBranch = dto.getBackendGitBranch();
        this.backendLanguage = dto.getBackendLanguage();
        this.backendFramework = dto.getBackendFramework();
        this.backendBuildTool = dto.getBackendBuildTool();
        this.frontendFramework = dto.getFrontendFramework();
        this.frontendBuildTool = dto.getFrontendBuildTool();
        this.frontendGitUrl = dto.getFrontendGitUrl();
        this.frontendGitBranch = dto.getFrontendGitBranch();
        this.databaseName = dto.getDatabaseName();
    }

}
