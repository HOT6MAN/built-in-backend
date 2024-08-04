package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.TeamProjectInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.example.hotsix.model.project.QTeamProjectInfo.teamProjectInfo;

@Repository
@RequiredArgsConstructor
public class BuildRepositoryImpl implements BuildRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public TeamProjectInfo findTeamProjectInfoByMemberAndInfoId(Long teamId, Long projectId) {
        return queryFactory.selectFrom(teamProjectInfo)
                .where(teamProjectInfo.team.id.eq(teamId).and(
                        teamProjectInfo.id.eq(projectId)
                ))
                .fetchOne();
    }
}
