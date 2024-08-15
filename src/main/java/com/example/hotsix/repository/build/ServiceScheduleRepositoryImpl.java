package com.example.hotsix.repository.build;

import com.example.hotsix.enums.BuildStatus;
import com.example.hotsix.model.QServiceSchedule;
import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.project.QTeamProjectInfo;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.hotsix.model.QServiceSchedule.serviceSchedule;
import static com.example.hotsix.model.QTeam.team;
import static com.example.hotsix.model.project.QTeamProjectInfo.teamProjectInfo;

@Repository
@RequiredArgsConstructor
public class ServiceScheduleRepositoryImpl implements ServiceScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public ServiceSchedule findServiceScheduleByServiceScheduleId(Long serviceScheduleId) {
        return queryFactory.selectFrom(serviceSchedule)
                .where(serviceSchedule.id.eq(serviceScheduleId))
                .fetchOne();
    }

    @Override
    public ServiceSchedule findEmptyService(TeamProjectInfo teamProjectInfo) {
        return queryFactory.selectFrom(serviceSchedule)
                .where(serviceSchedule.buildStatus.eq(BuildStatus.EMPTY)
                        .and(serviceSchedule.teamProjectInfo.isNull()
                                .or(serviceSchedule.teamProjectInfo.ne(teamProjectInfo))))
                .orderBy(serviceSchedule.id.asc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<TeamProjectInfo> findUsedProjectInfoByTeamId(Long teamId) {
        return queryFactory
                .selectFrom(QTeamProjectInfo.teamProjectInfo)
                .join(QTeamProjectInfo.teamProjectInfo.serviceSchedule, QServiceSchedule.serviceSchedule)
                .where(QServiceSchedule.serviceSchedule.team.id.eq(teamId)
                        .and(QServiceSchedule.serviceSchedule.buildStatus.eq(BuildStatus.SUCCESS)))
                .fetch();
    }
}
