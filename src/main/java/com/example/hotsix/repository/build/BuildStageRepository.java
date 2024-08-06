package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildStageRepository extends JpaRepository<BuildStage, Long> {

    List<BuildStage> findByBuildResultId(Long buildResultId);
}
