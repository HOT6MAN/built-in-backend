package com.example.hotsix.repository.team;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberTeamRepositoryImpl implements MemberTeamRepositoryCustom{
    private final JPAQueryFactory queryFactory;

}
