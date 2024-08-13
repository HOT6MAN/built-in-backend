package com.example.hotsix.controller.build;


import com.example.hotsix.dto.build.*;
import com.example.hotsix.dto.notification.GeneralResponseDto;
import com.example.hotsix.model.project.BuildResult;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.service.build.BuildService;
import com.example.hotsix.service.build.ServiceScheduleServiceImpl;
import com.example.hotsix.service.notification.NotificationService;
import com.example.hotsix.service.team.TeamProjectInfoService;
import com.example.hotsix.util.LocalTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
@Slf4j
public class BuildController {
    private final BuildService buildService;
    private final TeamProjectInfoService teamProjectInfoService;
    private final ServiceScheduleServiceImpl serviceScheduleServiceImpl;
    private final NotificationService notificationService;

    @PostMapping("/{teamId}/{projectInfoId}")
    public String insertTeamProjectCredential(@PathVariable("teamId") Long teamId,
                                                @PathVariable("projectInfoId") Long projectInfoId,
                                                @RequestBody ProjectInfoDto projectInfoDto)
    {
        teamProjectInfoService.insertAllTeamProjectInfo(teamId, projectInfoId, projectInfoDto);
        System.out.println("dtos = "+projectInfoDto);
        return null;
    }
    @PostMapping("/project/team/{teamId}/title/{title}")
    public TeamProjectInfo insertTeamProjectInfo(@PathVariable("teamId")Long teamId, @PathVariable("title") String title){
        return teamProjectInfoService.insertEmptyTeamProjectInfo(teamId, title);
    }

    @PostMapping("/project/team-project-info/{teamProjectInfoId}/deployNum/{deployNum}")
    public BuildResult insertBuildResult(@PathVariable("teamProjectInfoId") Long teamProjectInfoId, @PathVariable("deployNum") Long deployNum){
        return buildService.insertBuildResult(teamProjectInfoId, deployNum);
    }

    @PutMapping("/project/{projectInfoId}")
    public String updateProjectInfoNameByProjectInfoId(
            @PathVariable("projectInfoId") Long projectInfoId,
            @RequestBody Map<String, String> requestBody) {

        String updateProjectInfoName = requestBody.get("updateProjectInfoName");
        System.out.println("updateProjectInfoName = " + updateProjectInfoName);
        Boolean flag = teamProjectInfoService.updateProjectInfoNameByProjectInfoId(projectInfoId, updateProjectInfoName);
        if(flag) return "success";
        return "fail";
    }

    @PostMapping("/project/setup/{projectInfoId}")
    public String setUpDelopyConfigs(@PathVariable("projectInfoId") Long projectInfoId){
        return null;
    }

    @PostMapping("/project/backend/{projectInfoId}")
    public String saveBackendConfigs(@PathVariable("projectInfoId")Long projectInfoId,
                                     @RequestBody BackendConfigDto[] dtos){
        for(BackendConfigDto dto : dtos){
            System.out.println(dto);
        }
        Boolean flag = teamProjectInfoService.saveBackendConfigs(projectInfoId, dtos);
        if(flag) return "success";
        else return "fail";
    }
    @PostMapping("/project/frontend/{projectInfoId}")
    public String saveFrontendConfigs(@PathVariable("projectInfoId")Long projectInfoId,
                                      @RequestBody FrontendConfigDto[] dtos){
        for(FrontendConfigDto dto : dtos){
            System.out.println(dto);
        }
        Boolean flag = teamProjectInfoService.saveFrontendConfigs(projectInfoId, dtos);
        if(flag) return "success";
        else return "fail";
    }
    @PostMapping("/project/database/{projectInfoId}")
    public String saveDatabaseConfigs(@PathVariable("projectInfoId")Long projectInfoId,
                                      @RequestBody DatabaseConfigDto[] dtos){
        for(DatabaseConfigDto dto : dtos){
            System.out.println(dto);
        }
        Boolean flag = teamProjectInfoService.saveDatabaseConfigs(projectInfoId, dtos);
        if(flag) return "success";
        else return "fail";
    }
    @GetMapping("/project/{teamId}")
    public List<TeamProjectInfoDto> findAllProjectInfosByTeamId(@PathVariable("teamId")Long teamId){
        List<TeamProjectInfo> list = teamProjectInfoService.findAllProjectInfosByTeamId(teamId);
        List<TeamProjectInfoDto> returnList = new ArrayList<>();
        for(TeamProjectInfo entity: list){
            returnList.add(entity.toDto());
        }
        for(TeamProjectInfo teamProjectInfo : list){
            System.out.println(teamProjectInfo);
        }
        return returnList;
    }
    @GetMapping("/project/use/{teamId}")
    public List<TeamProjectInfoDto> findUsedProjectInfosByTeamId(@PathVariable("teamId")Long teamId){
        List<TeamProjectInfo> list = serviceScheduleServiceImpl.findUsedProjectInfoIdByTeamId(teamId);
        List<TeamProjectInfoDto> returnList = new ArrayList<>();
        for(TeamProjectInfo entity: list){
            TeamProjectInfoDto dto = entity.toDto();
            dto.setServiceScheduleId(entity.getServiceSchedule().getId());
            returnList.add(dto);
        }
        return returnList;
    }

    @PostMapping("/deploy/{teamId}/{projectInfoId}")
    public void buildStart(@PathVariable("teamId")Long teamId, @PathVariable("projectInfoId")Long projectInfoId){
        log.info("Build Start Call Team Id = {}, ProjectInfoId = {}", teamId, projectInfoId);
        buildService.buildStart(teamId, projectInfoId);
    }

    // whole build start API(built_in_full_docker를 돌림)
    @PostMapping("/deploy/project-info/{projectInfoId}")
    public BuildStartDto buildStart(@PathVariable("projectInfoId")Long projectInfoId){
        System.out.println("call build start");;

        return buildService.wholeBuildStart(projectInfoId);
    }

    @PostMapping("/deploy/{serviceNum}")
    public void buildStop(@PathVariable("serviceNum")Long serviceNum){
        System.out.println("Service Num = "+serviceNum);
        buildService.deployStop(serviceNum);
    }

    @PostMapping("/jenkins/result")
    public void jenkinsJobResult(@RequestBody JenkinsResultDto jenkinsResultDto) {
        log.info("jenkinsResultDto = " + jenkinsResultDto);


    }

    // Jenkins Build 결과를 DB에 저장하는 API
    @PostMapping("/deploy/result")
    public String saveBuildResult(@RequestBody BuildResultDto buildResultDto) throws Exception {
        log.info("save build start");

        log.info("buildResultDto = " + buildResultDto);

        // jenkins build 관련 log 저장
//        buildService.addWholeBuildResult(buildResultDto);

        // 알림 보내기 기능 테스트
        notificationService.sendGeneralResponse(GeneralResponseDto.builder()
                        .type("jenkins")
                        .receiverId(buildResultDto.getMemberId())
                        .notifyDate(LocalTimeUtil.getDateTime())
                        .response("테스트 성공!!!")
                        .build());
        return "success";
    }

    // {projectInfoId}에 해당하는 서비스를, 배포할 수 있는지 여부를 판단한다
    @GetMapping("/deploy/member/{memberId}/project-info/{projectInfoId}")
    public BuildCheckDto buildCheck(@PathVariable("memberId") Long memberId, @PathVariable("projectInfoId") Long projectInfoId){
        return buildService.buildCheck(memberId, projectInfoId);
    }

    // deploy_setup jenkins Job 실행
    @PostMapping("/project/setup")
    public void startJenkinsSetupJob(@RequestBody DeployConfig deployConfig) {
        System.out.println("deployConfig = " + deployConfig);

        buildService.startJenkisSetupJob(deployConfig);
    }

    // built_in_backend_docker jenkins Job 실행
    @PostMapping("/project/backend/member/{memberId}/team-project-info/{projectInfoId}/deployNum/{deployNum}/serviceNum/{serviceNum}")
    public void startJenkinsBackendJob(@PathVariable("memberId") Long memberId,
                                       @PathVariable("projectInfoId")Long projectInfoId,
                                       @PathVariable("deployNum") Long deployNum,
                                       @PathVariable("serviceNum") Long serviceNum,
                                       @RequestBody BackendConfigDto[] dtos) {
        for(BackendConfigDto dto : dtos){
            System.out.println(dto);
        }

        buildService.startJenkinsBackendJob(memberId, projectInfoId, deployNum, serviceNum, dtos);
        log.info("jenkins backend Job 실행 완료");
    }


    // Jenkins Build 결과를 불러오는 API
    @GetMapping("/deploy/result/team_project_info/{teamProjectInfoId}")
    public BuildWholeDto getBuildResultInfo(@PathVariable("teamProjectInfoId") Long teamProjectInfoId) {
        return buildService.getBuildResultInfo(teamProjectInfoId);
    }



}
