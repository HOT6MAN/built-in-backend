package com.example.hotsix.model.project;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "build_stage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildStage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_jenkins_job_id", referencedColumnName = "id")
    private BuildJenkinsJob buildJenkinsJob;

    @Column(name = "name")
    private String name;

    @Column(name = "stage_id")
    private Long stageId;

    @Column(name = "status")
    private String status;

    @Column(name = "duration")
    private Integer duration;

    @Override
    public String toString() {
        return "BuildStage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stageId=" + stageId +
                ", status='" + status + '\'' +
                ", duration=" + duration +
                '}';
    }
}
