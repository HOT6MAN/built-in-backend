package com.example.hotsix.repository.member;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.model.MemberImage;
import com.example.hotsix.model.Member;
import com.example.hotsix.vo.MemberVo;

import java.util.List;

public interface MemberRepositoryCustom {
    Member findMemberById(long id);

    MemberVo findPartialInfoById(long id);

    Member findMemberProfileByMemberId(Long id);

    void deleteMemberByMemberId(Long id);

    void updateMemberProfileByMemberId(MemberDto dto);

    void updateMemberProfileImageByMemberId(MemberImage dto);

    List<Member> findAllMemberByTeamId(Long teamId);

}