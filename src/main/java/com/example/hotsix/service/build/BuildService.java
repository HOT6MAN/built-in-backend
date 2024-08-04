package com.example.hotsix.service.build;

import com.example.hotsix.dto.build.ProjectInfoDto;
import com.example.hotsix.dto.build.TeamProjectCredentialDto;
import com.example.hotsix.dto.build.TeamProjectInfoDto;

public interface BuildService {
    boolean MemberProjectBuildStart(Long memberId, Long projectInfoId);
    void buildStart(Long teamId, Long teamProjectInfoId);
}
