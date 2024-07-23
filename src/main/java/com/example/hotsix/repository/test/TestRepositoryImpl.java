package com.example.hotsix.repository.test;

import com.example.hotsix.model.Member;
import com.example.hotsix.repository.Querydsl4RepositorySupport;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.vo.MemberVo;
import com.querydsl.core.types.Projections;

import static com.example.hotsix.model.QMember.member;

public class TestRepositoryImpl extends Querydsl4RepositorySupport implements TestRepositoryCustom {

    @Override
    public Member findMemberById(long id) {
        return selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

//    @Override
//    public MemberVo findPartialInfoById(long id) {
//        return select(Projections.bean(MemberVo.class,
//                member.id, member.nickname, member.deleted))
//                .from(member)
//                .where(member.id.eq(id))
//                .fetchOne();
//    }
}