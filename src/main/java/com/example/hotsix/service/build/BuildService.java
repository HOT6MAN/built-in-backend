package com.example.hotsix.service.build;

import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.project.BuildResult;

public interface BuildService {
    boolean MemberProjectBuildStart(Long memberId, Long projectInfoId);
    void buildStart(Long teamId, Long teamProjectInfoId);
    void deployStop(Long serviceScheduleId);

    BuildResult addWholeBuildResult(BuildResultDto buildResultDto) throws Exception;

    BuildWholeDto getBuildResultInfo(Long teamProjectInfoId);


    BuildStartDto wholeBuildStart(Long projectInfoId);

    BuildCheckDto buildCheck(Long memberId, Long projectInfoId);

    BuildResult insertBuildResult(Long teamProjectInfoId, Long deployNum);

    void startJenkisJob(DeployConfig deployConfig);

    void addMonitoringService(BuildResultDto buildResultDto) throws Exception;

}
