package com.example.hotsix.service.build;

import com.example.hotsix.dto.build.MemberProjectCredentialDto;
import com.example.hotsix.dto.build.MemberProjectInfoDto;

import java.io.IOException;

public interface BuildService {
    boolean insertMemberBuildInfo(Long memberId, MemberProjectCredentialDto buildDto, MemberProjectInfoDto projectInfoDto);
    MemberProjectCredentialDto getMemberBuildInfo(Long memberId);
    boolean MemberProjectBuildStart(Long memberId, Long projectInfoId);
}
