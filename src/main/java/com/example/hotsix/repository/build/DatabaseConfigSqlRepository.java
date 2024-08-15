package com.example.hotsix.repository.build;

import com.example.hotsix.model.DatabaseConfigSql;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseConfigSqlRepository extends JpaRepository<DatabaseConfigSql, Long>, DatabaseConfigSqlRepositoryCustom {
}
