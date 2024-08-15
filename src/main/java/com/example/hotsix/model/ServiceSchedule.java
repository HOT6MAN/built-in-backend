package com.example.hotsix.model;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.model.project.TeamProjectInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "service_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @OneToOne
    @JoinColumn(name = "team_project_info_id", referencedColumnName = "id")
    private TeamProjectInfo teamProjectInfo;

    @Column(name = "build_status")
    @Enumerated(EnumType.STRING)
    private BuildStatus buildStatus;

    @Column(name = "grafana_uid")
    private String grafanaUid;
}
