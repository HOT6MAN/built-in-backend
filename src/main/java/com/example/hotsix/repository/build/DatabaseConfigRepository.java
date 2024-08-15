package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.DatabaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfig, Long>, DatabaseConfigRepositoryCustom {
}
