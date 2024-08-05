package com.example.hotsix.model.project;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "build_num")
    private Long buildNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_result_id", referencedColumnName = "id")
    private TeamProjectInfo teamProjectInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BuildStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "build_time")
    private Date buildTime;
}
