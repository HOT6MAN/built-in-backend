package com.example.hotsix.controller.team;

import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.model.Team;
import com.example.hotsix.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
