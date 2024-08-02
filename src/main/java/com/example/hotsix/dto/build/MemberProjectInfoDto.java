package com.example.hotsix.dto.build;

import com.example.hotsix.model.Member;
import com.example.hotsix.model.MemberProjectInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProjectInfoDto {
    private Long id;
    private String backendGitURL;
    private String backendGitBranch;
    private String backendLanguage;
    private String backendFramework;
    private String backendBuildTool;
    private String frontendFramework;
    private String frontendBuildTool;
    private String frontendGitUrl;
    private String frontendGitBranch;
    private String databaseName;

    public MemberProjectInfo toEntity(){
        return MemberProjectInfo.builder()
                .id(id)
                .backendLanguage(backendLanguage)
                .backendBuildTool(backendBuildTool)
                .backendGitUrl(backendGitURL)
                .backendGitBranch(backendGitBranch)
                .backendFramework(backendFramework)
                .frontendFramework(frontendFramework)
                .frontendBuildTool(frontendBuildTool)
                .frontendGitUrl(frontendGitUrl)
                .frontendGitBranch(frontendGitBranch)
                .databaseName(databaseName)
                .build();
    }
}
