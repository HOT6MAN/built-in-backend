package com.example.hotsix.service.build;

import com.example.hotsix.dto.build.BuildResultDto;
import com.example.hotsix.dto.build.BuildResultInfoDto;
import com.example.hotsix.dto.build.BuildStartDto;
import com.example.hotsix.dto.build.BuildWholeDto;
import com.example.hotsix.model.project.BuildResult;

import java.net.URISyntaxException;
import java.util.List;

public interface BuildService {
    boolean MemberProjectBuildStart(Long memberId, Long projectInfoId);
    void buildStart(Long teamId, Long teamProjectInfoId);

    BuildResult addWholeBuildResult(BuildResultDto buildResultDto) throws Exception;

    BuildWholeDto getBuildResultInfo(Long teamProjectInfoId);


    BuildStartDto wholeBuildStart(Long projectInfoId);
}
