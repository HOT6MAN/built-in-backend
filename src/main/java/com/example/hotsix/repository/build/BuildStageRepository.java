package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildJenkinsJob;
import com.example.hotsix.model.project.BuildStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildStageRepository extends JpaRepository<BuildStage, Long> {

    List<BuildStage> findByBuildJenkinsJob(BuildJenkinsJob buildJenkinsJob);
}
