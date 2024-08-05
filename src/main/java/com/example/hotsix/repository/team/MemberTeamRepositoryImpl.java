package com.example.hotsix.repository.team;


import com.example.hotsix.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberTeamRepositoryImpl implements MemberTeamRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Team> findByMemberId(long memberId) {
        return List.of();
    }
}
