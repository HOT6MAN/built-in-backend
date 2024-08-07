package com.example.hotsix.controller.meeting;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.hotsix.service.meeting.MeetingService;

import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    /**
     * @param params The Session properties // 세션 속성 : (default) (설정할 필요 없음)
     * @return The Session ID
     */

    @PostMapping("/api/sessions") // session 생성 요청
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        System.out.println("post");
        return meetingService.initializeSession(params);
    }

    /**
     * @param sessionId The Session in which to create the Connection
     * @param params    The Connection properties
     * @return The Session
     */
    @GetMapping("/api/sessions/{sessionId}") // session 접근 요청
    public ResponseEntity<String> getSession(@PathVariable("sessionId") String sessionId,
                                             @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return meetingService.getSession(sessionId, params);
    }
    /**
     * @param params
     * {
     *     "event": "sessionDestroyed", (세션 이벤트) <- destroyed일 때만 webhook 들어옴
     *     "timestamp": 1601395365656,
     *     "sessionId": "ses_BMS9K6aKeN", (세션 아이디) <- 얘만 가져가서 조회 후 삭제
     *     "startTime": 1601394690713,
     *     "duration": 674, (지속 시간, sec)
     *     "reason": "lastParticipantLeft" (세션 종료 원인)
     * }
     * @return The Session
     */
    @PostMapping("/api/sessions/webhook") // session 삭제 시 openvidu 서버에서 webhook 요청
    public ResponseEntity<String> deleteSession(@RequestBody Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return meetingService.deleteSession(params);
    }
}




