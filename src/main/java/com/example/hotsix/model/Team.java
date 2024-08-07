package com.example.hotsix.model;

import com.example.hotsix.enums.TeamStatus;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.example.hotsix.dto.team.TeamDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Table(name="team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @Column(name="content")
    private String content;

    @CreatedDate
    @Column(name="start_time")
    private LocalDateTime startTime;

    @LastModifiedDate
    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name="git_url")
    private String gitUrl;

    @Column(name="jira_url")
    private String jiraUrl;

    @Column(name="session_id")
    private String sessionId;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private ServiceSchedule schedules;

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamProjectInfo> teamProjectInfos = new ArrayList<>();
    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();
    @OneToMany(mappedBy = "team")
    private List<Meeting> meetings;


//    @OneToMany
//    @JoinColumn(name = "team_id")
//    private List<MemberTeam> memberTeams = new ArrayList<>();

//    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private TeamProjectCredential teamProjectCredential;

    public TeamDto toDto(){
        return TeamDto.builder()
                .id(id)
                .name(name)
                .status(String.valueOf(status))
                .content(content)
                .startTime(startTime)
                .endTime(endTime)
                .gitUrl(gitUrl)
                .jiraUrl(jiraUrl)
                .sessionId(sessionId)
                .memberTeams(memberTeams.stream()
                        .map(MemberTeam::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public void addTeamProjectInfo(TeamProjectInfo teamProjectInfo) {
        teamProjectInfos.add(teamProjectInfo);
        teamProjectInfo.setTeam(this);
    }

    public void removeTeamProjectInfo(TeamProjectInfo teamProjectInfo) {
        teamProjectInfos.remove(teamProjectInfo);
        teamProjectInfo.setTeam(null);
    }
//    public void setMemberProjectCredential(TeamProjectCredential teamProjectCredential) {
//        this.teamProjectCredential = teamProjectCredential;
//        teamProjectCredential.setTeam(this);
//    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", gitUrl='" + gitUrl + '\'' +
                ", jiraUrl='" + jiraUrl + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", memberTeams=" + memberTeams +
                '}';
    }

}
