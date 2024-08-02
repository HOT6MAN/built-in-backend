package com.example.hotsix.repository.build;

import com.example.hotsix.model.MemberProjectInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QMemberProjectInfo.memberProjectInfo;

@Repository
@RequiredArgsConstructor
public class BuildRepositoryImpl implements BuildRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public MemberProjectInfo findMemberProjectInfoByMemberAndInfoId(Long memberId, Long projectId) {
        return queryFactory.selectFrom(memberProjectInfo)
                .where(memberProjectInfo.member.id.eq(memberId).and(
                        memberProjectInfo.id.eq(projectId)
                ))
                .fetchOne();
    }
}
