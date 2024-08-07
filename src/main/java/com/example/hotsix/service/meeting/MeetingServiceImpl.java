package com.example.hotsix.service.meeting;

import com.example.hotsix.model.Team;
import com.example.hotsix.repository.team.TeamRepository;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MeetingServiceImpl implements MeetingService {


    private OpenVidu openvidu;
    private final TeamRepository teamRepository;

    public MeetingServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Override
    public ResponseEntity<String> initializeSession(Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {

        // 세션 속성 생성
        SessionProperties properties = SessionProperties.fromJson(params).build();
        // 새 세션 생성
        Session session = openvidu.createSession(properties);

        // 클라이언트에서 전달된 teamId 가져오기
        Long teamId = getTeamId(params);
        if (teamId == null) {
            return new ResponseEntity<>("teamId is required and should be a number", HttpStatus.BAD_REQUEST);
        }
        // 팀 찾아서 세션 ID 업데이트
        Team team = updateTeamSessionId(teamId, session.getSessionId());
        if (team == null) {
            return new ResponseEntity<>("Invalid teamId: " + teamId, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getSession(String sessionId, Map<String, Object> params) throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>("session not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("show me the session: " + session.getSessionId(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteSession(Map<String, Object> params) throws OpenViduJavaClientException, OpenViduHttpException {
        String sessionId = getSessionId(params);
        System.out.println("sessionId: " + sessionId);
        if (sessionId == null) {
            return new ResponseEntity<>("sessionId is required", HttpStatus.BAD_REQUEST);
        }

        Optional<Team> team = deleteTeamSessionId(sessionId);
        if (team.isPresent()) {
            return new ResponseEntity<>("session deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("session not found", HttpStatus.NOT_FOUND);
        }
    }

    private Long getTeamId(Map<String, Object> params) {
        try {
            return params.get("teamId") != null ? ((Number) params.get("teamId")).longValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    private String getSessionId(Map<String, Object> params) {
        try {
            return params.get("sessionId") != null ? ((String) params.get("sessionId")) : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    private Team updateTeamSessionId(Long teamId, String sessionId) {
        return teamRepository.findById(teamId).map(team -> {
            team.setSessionId(sessionId);
            return teamRepository.save(team);
        }).orElse(null);
    }

    private Optional<Team> deleteTeamSessionId(String sessionId) {
        return teamRepository.findTeamBySessionId(sessionId).map(team -> {
            team.setSessionId(null);
            teamRepository.save(team);
            return team;
        });
    }
}
