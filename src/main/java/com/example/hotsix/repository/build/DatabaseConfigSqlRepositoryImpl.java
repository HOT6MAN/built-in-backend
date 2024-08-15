package com.example.hotsix.repository.build;

import com.example.hotsix.model.DatabaseConfigSql;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QDatabaseConfigSql.databaseConfigSql;

@Repository
@RequiredArgsConstructor
public class DatabaseConfigSqlRepositoryImpl implements DatabaseConfigSqlRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public DatabaseConfigSql findDatabaseConfigSqlByDatabaseConfig(Long id) {
        return queryFactory.selectFrom(databaseConfigSql)
                .where(databaseConfigSql.databaseConfig.id.eq(id))
                .fetchOne();
    }
}
