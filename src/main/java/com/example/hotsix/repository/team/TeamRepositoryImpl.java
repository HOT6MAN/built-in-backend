package com.example.hotsix.repository.team;

import com.example.hotsix.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Team findAllTeamByMemberId(Long id) {
        return null;
    }

    @Override
    public Team findTeamByTeamId(Long teamId) {
        return queryFactory.selectFrom(team)
                .where(team.id.eq(teamId))
                .fetchOne();
    }
}
