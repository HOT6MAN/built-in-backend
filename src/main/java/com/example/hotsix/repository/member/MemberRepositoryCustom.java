package com.example.hotsix.repository.test;

import com.example.hotsix.model.Member;
import com.example.hotsix.vo.MemberVo;

public interface TestRepositoryCustom {
    Member findMemberById(long id);

//    MemberVo findPartialInfoById(long id);
}