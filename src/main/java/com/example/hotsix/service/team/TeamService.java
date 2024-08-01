package com.example.hotsix.service.team;

import com.example.hotsix.model.Team;

import java.util.List;

public interface TeamService {
// 팀 생성 해야함

    void createTeam(Team team, Long memberId);
    List<Team> getAllMyTeams();

    Team getTeamById(int id);
}
