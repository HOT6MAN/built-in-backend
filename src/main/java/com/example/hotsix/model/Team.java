package com.example.hotsix.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

//    @OneToMany
//    @JoinColumn(name = "team_id")
//    private List<MemberTeam> memberTeams = new ArrayList<>();




}
