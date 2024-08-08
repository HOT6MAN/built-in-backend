package com.example.hotsix.repository.member;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.model.MemberImage;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.Querydsl4RepositorySupport;
import com.example.hotsix.vo.MemberVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QMember.member;
import static com.example.hotsix.model.QMemberImage.memberImage;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl extends Querydsl4RepositorySupport implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final MemberImageRepository memberImageRepository;
    @Override
    public Member findMemberById(long id) {
        return selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public MemberVo findPartialInfoById(long id) {
        return select(Projections.bean(MemberVo.class,
                member.id, member.nickname, member.deleted))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public Member findMemberProfileByMemberId(Long id) {
        return queryFactory.selectFrom(member)
                .leftJoin(member.memberImage).fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public void deleteMemberByMemberId(Long id) {
        queryFactory.update(member)
                .set(member.deleted, true)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    public void updateMemberProfileByMemberId(MemberDto dto) {
        System.out.println("call update Impl dto = "+dto);
        queryFactory.update(member)
                .set(member.address, dto.getAddress())
                .set(member.phone, dto.getPhone())
                .set(member.nickname, dto.getNickname())
                .where(member.id.eq(dto.getId()))
                .execute();
    }

    @Override
    public void updateMemberProfileImageByMemberId(MemberImage dto) {

        queryFactory.update(memberImage)
                .set(memberImage.saveFolder, dto.getSaveFolder())
                .set(memberImage.fixedName, dto.getFixedName())
                .set(memberImage.originName, dto.getOriginName())
                .set(memberImage.type, dto.getType())
                .where(memberImage.member.id.eq(dto.getMember().getId()))
                .execute();
    }
}