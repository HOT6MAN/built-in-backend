package com.example.hotsix.controller.team;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.Team;
import com.example.hotsix.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/teams")
    public Team createTeam(@RequestBody TeamDto teamDto) {
        log.info("teamDto: {}", teamDto.toString());

        Team team = Team.builder()
                .name(teamDto.getName())
                .content(teamDto.getContent())
                .build();
        Long memberId = teamDto.getMemberId();

        teamService.createTeam(team, memberId);
        return team;
    }

    @GetMapping("/teams/{memberId}")
    public List<TeamDto> getAllMyTeams(@PathVariable("memberId") String memberId) {
        log.info("memberId: {}",memberId);
        Long id = Long.valueOf(memberId);


        List<TeamDto> allMyTeams = teamService.getAllMyTeams(Long.valueOf(id));


        return allMyTeams;
    }

    @GetMapping("/teams/{teamId}/detail")
    public TeamDto getMyTeam(@PathVariable("teamId") String teamId) {
        TeamDto teamDto = teamService.getTeamById(Long.valueOf(teamId));
        return teamDto;
    }

    @PatchMapping("/teams/jira")
    public TeamDto updateJiraUrl(@RequestBody TeamDto teamDto){
        teamDto = teamService.updateJiraUrl(teamDto.getJiraUrl(), teamDto.getId());
        return teamDto;
    }

    @PatchMapping("/teams/git")
    public TeamDto updateGitUrl(@RequestBody TeamDto teamDto){
        teamDto = teamService.updateGitUrl(teamDto.getGitUrl(), teamDto.getId());
        return teamDto;
    }

    @PatchMapping("/teams/status")
    public TeamDto updateStatus(@RequestBody TeamDto teamDto){
        return teamService.updateStatus(teamDto.getId());
    }

}
