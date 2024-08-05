package com.example.hotsix.repository.team;


import com.example.hotsix.model.Team;

import java.util.List;

public interface MemberTeamRepositoryCustom {

    List<Team> findByMemberId(long memberId);
}
