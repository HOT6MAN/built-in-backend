package com.example.hotsix.repository.build;

import com.example.hotsix.model.DatabaseConfigSql;

public interface DatabaseConfigSqlRepositoryCustom {
    DatabaseConfigSql findDatabaseConfigSqlByDatabaseConfig(Long id);
}
