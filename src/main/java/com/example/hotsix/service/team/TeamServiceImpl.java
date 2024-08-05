package com.example.hotsix.service.team;

import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.enums.TeamStatus;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.MemberTeam;
import com.example.hotsix.model.Team;
import com.example.hotsix.repository.team.MemberTeamRepository;
import com.example.hotsix.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.hotsix.enums.TeamStatus.FINISH;
import static com.example.hotsix.enums.TeamStatus.RECRUIT;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Override
    public void createTeam(Team team, Long memberId) {
        team.setStatus(RECRUIT);
        teamRepository.save(team);

        Member member = Member.builder().
                id(memberId).build();

        MemberTeam memberTeam = MemberTeam.builder()
                .member(member)
                .team(team)
                .leader(true)

                .build();
        memberTeamRepository.save(memberTeam);
    }

    @Override
    public List<TeamDto> getAllMyTeams(Long memberId) {
        List<Team> allTeamByMemberId = teamRepository.findAllTeamByMemberId(memberId);
        List<TeamDto> teamDtos=
                allTeamByMemberId.stream()
                .map(Team::toDto)
                .collect(Collectors.toList());

        log.info("allTeamByMemberId: {}", teamDtos.toString());

        return teamDtos;
    }

    @Override
    public TeamDto getTeamById(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        return team
                .map(Team::toDto)
                .orElse(null);
    }

    @Override
    public TeamDto updateJiraUrl(String jiraUrl, Long teamId) {
        Team team = teamRepository.findTeamById(teamId);
        team.setJiraUrl(jiraUrl);
        return team.toDto();
    }

    @Override
    public TeamDto updateGitUrl(String gitUrl, Long teamId) {
        Team team = teamRepository.findTeamById(teamId);
        team.setGitUrl(gitUrl);
        return team.toDto();
    }

    @Override
    public TeamDto updateStatus(Long teamId) {
        Team team = teamRepository.findTeamById(teamId);
        if(team.getStatus().equals(RECRUIT)) {
            team.setStatus(FINISH);
        }else{
            team.setStatus(RECRUIT);
        }
        return team.toDto();
    }
}
