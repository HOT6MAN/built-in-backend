package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildLog;
import com.example.hotsix.model.project.BuildStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildLogRepository extends JpaRepository<BuildLog, Long> {

    List<BuildLog> findByBuildStageId(Long buildStageId);
}
