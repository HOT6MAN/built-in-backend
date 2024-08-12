package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.BuildJenkinsJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildJenkinsJobRepository extends JpaRepository<BuildJenkinsJob, Long> {
}
