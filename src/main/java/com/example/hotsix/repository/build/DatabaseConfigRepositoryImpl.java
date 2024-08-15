package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.DatabaseConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.project.QDatabaseConfig.databaseConfig;

@Repository
@RequiredArgsConstructor
public class DatabaseConfigRepositoryImpl implements DatabaseConfigRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public DatabaseConfig findDatabaseConfigById(Long id) {
        return queryFactory.selectFrom(databaseConfig)
                .where(databaseConfig.id.eq(id))
                .fetchOne();
    }
}
