package com.example.hotsix.model.project;

import com.example.hotsix.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "build_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_stage_id", referencedColumnName = "id")
    private BuildStage buildStage;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
