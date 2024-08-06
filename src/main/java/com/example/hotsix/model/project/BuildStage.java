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
    @JoinColumn(name = "build_result_id", referencedColumnName = "id")
    private BuildResult buildResult;

    @Column(name = "name")
    private String name;

    @Column(name = "stage_id")
    private Long stageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BuildStatus status;

    @Column(name = "duration")
    private Integer duration;
}
