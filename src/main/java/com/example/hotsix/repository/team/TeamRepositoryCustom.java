package com.example.hotsix.repository.team;

import com.example.hotsix.model.Team;

public interface TeamRepositoryCustom {

    Team findAllTeamByMemberId(Long id);

}
