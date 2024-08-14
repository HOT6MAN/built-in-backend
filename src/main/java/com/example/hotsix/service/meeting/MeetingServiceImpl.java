package com.example.hotsix.service.meeting;

import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.Team;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.repository.team.TeamRepository;
import com.example.hotsix.service.notification.NotificationService;
import com.example.hotsix.util.LocalTimeUtil;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MeetingServiceImpl implements MeetingService {


    private OpenVidu openvidu;
    private final TeamRepository teamRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;


    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Override
    public TeamDto createSession(Long teamId, Long memberId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        //openvidu 서버에 session 생성 요청
        Map<String, Object> params = Map.of();
        String sessionId = initializeSession(params);

        List<Member> members = memberRepository.findAllMemberByTeamId(teamId);
        for(Member member : members) {
            notificationService.send(memberId, member.getId(), "RTC");
            System.out.println("Meeting Service Impl Create Session Notification send");
            log.info("Sender = {}, receiver = {}, Type = {}", memberId, member.getId(), "RTC");
            Notification notification = Notification.builder()
                    .receiver(member.getId())
                    .sender(memberId)
                    .type("RTC")
                    .url("/")
                    .isRead(false)
                    .notifyDate(LocalTimeUtil.getDateTime())
                    .build();
            notificationService.save(notification);
        }
        // sessionId 저장 후 TeamDto return
        return updateSessionId(sessionId, teamId);

    }

    @Override
    public TeamDto getSession(Long teamId) {
        Team team = teamRepository.findTeamById(teamId);
        String sessionId = team.getSessionId();

        // sessionId 있는데 openvidu 서버에서 활성화가 안 됐을 때
        if (sessionId != null) {
            Session activeSession = openvidu.getActiveSession(sessionId);
            if (activeSession == null) {
                // sessionId 초기화
                team.setSessionId(null);
                teamRepository.save(team);
                throw new BuiltInException(Process.MEETING_NOT_FOUND);
            }
        }
        return getTeamById(teamId);
    }

    @Override
    public ResponseEntity<String> deleteSession(Map<String, Object> params) {
        String sessionId = getSessionIdFromParam(params);
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

    private String initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);
        return session.getSessionId();
    }

    private TeamDto updateSessionId(String sessionId, Long teamId) {
        Team team = teamRepository.findTeamById(teamId);
        team.setSessionId(sessionId);
        teamRepository.save(team);
        return team.toDto();
    }

    private TeamDto getTeamById(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        return team
                .map(Team::toDto)
                .orElse(null);
    }

    private String getSessionIdFromParam(Map<String, Object> params) {
        try {
            return params.get("sessionId") != null ? ((String) params.get("sessionId")) : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    private Optional<Team> deleteTeamSessionId(String sessionId) {
        return teamRepository.findTeamBySessionId(sessionId).map(team -> {
            team.setSessionId(null);
            teamRepository.save(team);
            return team;
        });
    }

}
