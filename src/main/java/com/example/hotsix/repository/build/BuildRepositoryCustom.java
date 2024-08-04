package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.TeamProjectInfo;

public interface BuildRepositoryCustom {
    TeamProjectInfo findTeamProjectInfoByMemberAndInfoId(Long memberId, Long projectId);
}
