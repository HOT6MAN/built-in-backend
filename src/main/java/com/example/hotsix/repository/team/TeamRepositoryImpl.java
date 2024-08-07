package com.example.hotsix.repository.team;

import com.example.hotsix.model.MemberTeam;
import com.example.hotsix.model.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.example.hotsix.repository.Querydsl4RepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.example.hotsix.model.QMemberTeam.memberTeam;
import static com.example.hotsix.model.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl extends Querydsl4RepositorySupport implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findAllTeamByMemberId(Long memberId) {


// Subquery to find team IDs associated with the given member ID
        JPAQuery<Long> subQuery = queryFactory
                .select(memberTeam.team.id)
                .from(memberTeam)
                .where(memberTeam.member.id.eq(memberId));

        // Main query to select team information
        List<Team> teams = queryFactory
                .selectFrom(team)
                .where(team.id.in(subQuery))
                .fetch();

        // Additional query to get member-team associations
//        List<MemberTeam> memberTeams = queryFactory
//                .selectFrom(memberTeam)
//                .where(memberTeam.team.id.in(subQuery))
//                .fetch();

        return teams;
    }

    @Override
    public Team findTeamById(Long id) {
        return queryFactory.selectFrom(team)
                .where(team.id.eq(id))
                .fetchOne();
    }

    public Optional<Team> findTeamBySessionId(String sessionId) {
        if (sessionId == null) {
            return Optional.empty();
        }
        Team result = queryFactory.selectFrom(team)
                .where(team.sessionId.eq(sessionId))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
