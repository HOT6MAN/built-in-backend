package com.example.hotsix.service.meeting;

import java.util.Map;

import com.example.hotsix.dto.team.TeamDto;
import org.springframework.http.ResponseEntity;

import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

public interface MeetingService {
    TeamDto createSession(Long teamId)
            throws OpenViduJavaClientException, OpenViduHttpException;
    TeamDto getSession(Long teamId)
            throws OpenViduJavaClientException, OpenViduHttpException;
    ResponseEntity<String> deleteSession(Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException;
}


