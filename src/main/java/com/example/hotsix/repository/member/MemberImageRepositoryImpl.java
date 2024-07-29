package com.example.hotsix.repository.member;

import com.example.hotsix.model.MemberImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QMemberImage.memberImage;


@Repository
@RequiredArgsConstructor
public class MemberImageRepositoryImpl implements MemberImageRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public MemberImage findImageByMemberId(Long id) {
        return queryFactory.selectFrom(memberImage)
                .where(memberImage.member.id.eq(id))
                .fetchOne();
    }
}
