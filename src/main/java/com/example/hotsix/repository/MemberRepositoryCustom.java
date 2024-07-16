package com.example.hotsix.repository;

import com.example.hotsix.model.Member;
import com.example.hotsix.vo.MemberVo;

public interface MemberRepositoryCustom {
    Member findMemberById(long id);

    MemberVo findPartialInfoById(long id);
}