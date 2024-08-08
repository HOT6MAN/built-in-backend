package com.example.hotsix.controller.meeting;


import java.util.Map;

import com.example.hotsix.dto.team.TeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.example.hotsix.service.meeting.MeetingService;

import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/meeting/api")
public class MeetingController {

    private final MeetingService meetingService;

    @PatchMapping("/sessions/{teamId}/create") // session 생성 요청
    public TeamDto createSession(@PathVariable("teamId") Long teamId)
            throws OpenViduJavaClientException, OpenViduHttpException {

        return meetingService.createSession(teamId);
    }

    @GetMapping("/sessions/{teamId}/get") // session 접근 요청
    public TeamDto getSession(@PathVariable("teamId") Long teamId)
            throws OpenViduJavaClientException, OpenViduHttpException {

        return meetingService.getSession(teamId);
    }

    @PostMapping("/sessions/webhook") // session 삭제 시 openvidu 서버에서 webhook 요청
    public ResponseEntity<String> deleteSession(@RequestBody Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {

        return meetingService.deleteSession(params);
    }
}




