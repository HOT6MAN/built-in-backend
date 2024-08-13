package com.example.hotsix.model.project;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.enums.JenkinsJobType;
import com.example.hotsix.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "build_jenkins_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildJenkinsJob extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_result_id", referencedColumnName = "id")
    private BuildResult buildResult;

    // jenkins job 기준, 몇 번째로 한 빌드인지
    @Column(name = "build_num")
    private Long buildNum;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "result")
    private BuildStatus result;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JenkinsJobType jobType;

}
