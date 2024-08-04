package com.example.hotsix.repository.team;

import com.example.hotsix.model.project.TeamProjectInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.hotsix.model.project.QTeamProjectInfo.teamProjectInfo;

@Repository
@RequiredArgsConstructor
public class TeamProjectInfoRepositoryImpl implements TeamProjectInfoRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<TeamProjectInfo> findAllProjectInfosByTeamId(Long teamId) {
        return queryFactory.selectFrom(teamProjectInfo)
                .where(teamProjectInfo.team.id.eq(teamId))
                .fetch();
    }
    @Override
    public TeamProjectInfo findProjectInfoByProjectInfoId(Long projectInfoId){
        return queryFactory.selectFrom(teamProjectInfo)
                .where(teamProjectInfo.id.eq(projectInfoId))
                .fetchOne();
    }
}
