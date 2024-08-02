package com.example.hotsix.repository.build;

import com.example.hotsix.model.MemberProjectInfo;

public interface BuildRepositoryCustom {
    MemberProjectInfo findMemberProjectInfoByMemberAndInfoId(Long memberId, Long projectId);
}
