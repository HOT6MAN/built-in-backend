package com.example.hotsix.controller.team;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.Team;
import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/teams/{teamId}")
    public void deleteTeam(@PathVariable("teamId") String teamId){
        teamService.deleteTeam(Long.valueOf(teamId));
    }


    @GetMapping("/teams/{memberId}")
    public List<TeamDto> getAllMyTeams(@PathVariable("memberId") String memberId) {
        log.info("memberId: {}",memberId);
        Long id = Long.valueOf(memberId);


        List<TeamDto> allMyTeams = teamService.getAllMyTeams(Long.valueOf(id));


        return allMyTeams;
    }

    @GetMapping("/teams/{teamId}/detail")
    public TeamDto getMyTeam(@PathVariable("teamId") String teamId, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        log.info("getMyTeam Controller");
        TeamDto teamDto = teamService.getTeamById(Long.valueOf(teamId));
        // 존재하지 않는 팀일 경우
        if(teamDto == null) {
            throw new BuiltInException(Process.TEAM_NOT_FOUND);
        }

        long count = teamDto.getMemberTeams().stream()
                .filter(item -> item.getMember().getId().equals(oAuth2User.getId()))
                .count();

        //팀에 속하지 않은 유저일 경우
        if(count !=1){
            throw new BuiltInException(Process.UNAUTHORIZED_USER);
        }

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
