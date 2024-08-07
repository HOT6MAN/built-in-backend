//package com.example.hotsix.model;
//
//import com.example.hotsix.dto.build.TeamProjectCredentialDto;
//import jakarta.persistence.*;
//import lombok.*;
//import net.minidev.json.annotate.JsonIgnore;
//
//@Entity(name = "member_project_credential")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class TeamProjectCredential {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
////    @JsonIgnore
////    @OneToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "team_id", referencedColumnName = "id")
////    private Team team;
//
//    @Column(name = "job_name")
//    private String jobName;
//
//    @Column(name = "git_username")
//    private String gitUsername;
//
//    @Column(name = "git_token")
//    private String gitToken;
//
//    @Column(name = "docker_username")
//    private String dockerUsername;
//
//    @Column(name = "docker_token")
//    private String dockerToken;
//
//    @Column(name = "git_credential_id")
//    private String gitCredentialId;
//
//    @Column(name = "docker_credential_id")
//    private String dockerCredentialId;
//
//    public TeamProjectCredentialDto toDto() {
//        return TeamProjectCredentialDto.builder()
//                .id(id)
//                .jobName(jobName)
//                .gitUsername(gitUsername)
//                .gitToken(gitToken)
//                .dockerUsername(dockerUsername)
//                .dockerToken(dockerToken)
//                .gitCredentialId(gitCredentialId)
//                .dockerCredentialId(dockerCredentialId)
//                .build();
//    }
//
//    public void setPropertiesFromDto(TeamProjectCredentialDto dto) {
//        this.id = dto.getId();
//        this.gitUsername = dto.getGitUsername();
//        this.gitToken = dto.getGitToken();
//        this.dockerUsername = dto.getDockerUsername();
//        this.dockerToken = dto.getDockerToken();
//        this.gitCredentialId = dto.getGitCredentialId();
//        this.dockerCredentialId = dto.getDockerCredentialId();
//    }
//}
