package com.example.hotsix.repository.build;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QServiceSchedule.serviceSchedule;

@Repository
@RequiredArgsConstructor
public class ServiceScheduleRepositoryImpl implements ServiceScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long findEmptyService() {
        return queryFactory.selectFrom(serviceSchedule)
                .where(serviceSchedule.isUsed.eq(false))
                .orderBy(serviceSchedule.id.asc())
                .limit(1)
                .fetchOne().getId();
    }
}
