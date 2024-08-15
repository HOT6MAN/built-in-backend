package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.DatabaseConfig;

public interface DatabaseConfigRepositoryCustom {
    DatabaseConfig findDatabaseConfigById(Long id);
}
