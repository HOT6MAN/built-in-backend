package com.example.hotsix.repository.team;

import com.example.hotsix.model.Team;

import java.util.List;

public interface TeamRepositoryCustom {

    List<Team> findAllTeamByMemberId(Long id);
    //Team findTeamById(Long id);
}
