package com.example.hotsix.controller.build;


import com.example.hotsix.dto.build.*;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.service.build.BuildService;
import com.example.hotsix.service.build.ServiceScheduleServiceImpl;
import com.example.hotsix.service.team.TeamProjectInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
@Slf4j
public class BuildController {
    private final BuildService buildService;
    private final TeamProjectInfoService teamProjectInfoService;
    private final ServiceScheduleServiceImpl serviceScheduleServiceImpl;

    @PostMapping("/{teamId}/{projectInfoId}")
    public String insertTeamProjectCredential(@PathVariable("teamId") Long teamId,
                                                @PathVariable("projectInfoId") Long projectInfoId,
                                                @RequestBody ProjectInfoDto projectInfoDto)
    {
        teamProjectInfoService.insertAllTeamProjectInfo(teamId, projectInfoId, projectInfoDto);
        System.out.println("dtos = "+projectInfoDto);
        return null;
    }
    @PostMapping("/project/{teamId}")
    public String insertTeamProjectInfo(@PathVariable("teamId")Long teamId){
        teamProjectInfoService.insertEmptyTeamProjectInfo(teamId);
        return "success";
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


    // Jenkins Build 결과를 DB에 저장하는 API
    @PostMapping("/deploy/result")
    public String saveBuildResult(@RequestBody BuildResultDto buildResultDto) throws Exception {
        System.out.println("save build start");

        // jenkins build 관련 log 저장
        buildService.addWholeBuildResult(buildResultDto);

        return "success";
    }

    // Jenkins Build 결과를 불러오는 API
    @GetMapping("/deploy/result/team_project_info/{teamProjectInfoId}")
    public BuildWholeDto getBuildResultInfo(@PathVariable("teamProjectInfoId") Long teamProjectInfoId) {
        return buildService.getBuildResultInfo(teamProjectInfoId);
    }
}
