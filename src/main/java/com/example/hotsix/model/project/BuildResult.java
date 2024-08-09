package com.example.hotsix.model.project;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "build_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildResult extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_project_info_id", referencedColumnName = "id")
        private TeamProjectInfo teamProjectInfo;

        @Column(name = "deploy_num")
        private Long deployNum;

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private BuildStatus status;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "build_time")
        private LocalDateTime buildTime;

        @Column(name = "grafana_uid")
        private String grafanaUid;
}
