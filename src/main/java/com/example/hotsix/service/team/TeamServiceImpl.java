package com.example.hotsix.service.team;

import com.example.hotsix.model.MemberTeam;
import com.example.hotsix.model.Team;
import com.example.hotsix.repository.team.MemberTeamRepository;
import com.example.hotsix.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Override
    public void createTeam(Team team, Long memberId) {
        teamRepository.save(team);
        Long teamId = team.getId();

        MemberTeam memberTeam = MemberTeam.builder()
                .memberId(memberId)
                .teamId(teamId)
                .leader(true)
                .build();
        memberTeamRepository.save(memberTeam);
    }

    @Override
    public List<Team> getAllMyTeams() {
        return List.of();
    }

    @Override
    public Team getTeamById(int id) {
        return null;
    }
}
