package com.example.hotsix.model;

import com.example.hotsix.model.project.TeamProjectInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String status;

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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSchedule> schedules = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamProjectInfo> teamProjectInfos = new ArrayList<>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TeamProjectCredential teamProjectCredential;


    public void addTeamProjectInfo(TeamProjectInfo teamProjectInfo) {
        teamProjectInfos.add(teamProjectInfo);
        teamProjectInfo.setTeam(this);
    }

    public void removeTeamProjectInfo(TeamProjectInfo teamProjectInfo) {
        teamProjectInfos.remove(teamProjectInfo);
        teamProjectInfo.setTeam(null);
    }
    public void setMemberProjectCredential(TeamProjectCredential teamProjectCredential) {
        this.teamProjectCredential = teamProjectCredential;
        teamProjectCredential.setTeam(this);
    }
}
