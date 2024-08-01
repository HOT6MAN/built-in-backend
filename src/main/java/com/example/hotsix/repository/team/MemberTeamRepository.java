package com.example.hotsix.repository.team;

import com.example.hotsix.model.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long>, MemberTeamRepositoryCustom {

}
