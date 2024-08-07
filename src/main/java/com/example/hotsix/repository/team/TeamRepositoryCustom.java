package com.example.hotsix.repository.team;

import com.example.hotsix.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepositoryCustom {

    List<Team> findAllTeamByMemberId(Long id);
    Team findTeamById(Long id);
    Optional<Team> findTeamBySessionId(String sessionId);
}
