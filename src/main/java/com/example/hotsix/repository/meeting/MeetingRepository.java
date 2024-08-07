package com.example.hotsix.repository.meeting;

import com.example.hotsix.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotsix.model.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    boolean existsByTeam(Team team);
    Meeting findBySessionId(String sessionId);
}

