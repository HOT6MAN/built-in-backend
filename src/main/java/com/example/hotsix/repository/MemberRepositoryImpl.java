package com.example.hotsix.repository;

import com.example.hotsix.model.Member;

import static com.example.hotsix.model.QMember.member;

public class MemberRepositoryImpl extends Querydsl4RepositorySupport<Member> implements MemberRepositoryCustom{
    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Member findMemberById(long id) {
        return getQueryFactory()
                .selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }
}
