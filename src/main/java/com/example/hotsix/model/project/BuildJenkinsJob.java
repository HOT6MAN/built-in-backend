package com.example.hotsix.model.project;

import com.example.hotsix.enums.BuildStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "build_jenkins_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildJenkinsJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_result_id", referencedColumnName = "id")
    private BuildResult buildResult;

    @Column(name = "build_number")
    private Long buildNumber;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "result")
    private BuildStatus result;

    @Column(name = "job_type")
    private String jobType;
}
